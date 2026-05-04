package com.mobileApplication.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileApplication.dto.SymptomRequest;
import com.mobileApplication.models.SymptomAnalysis;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    //gemini-2.5-flash-lite
    private static final String[] MODEL_URLS = {
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent",
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent",
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-pro:generateContent"
    };

    private static final int[] RPD_LIMITS = { 1500, 500, 25 };
    private final AtomicInteger[] rpdUsed  = {
        new AtomicInteger(0),
        new AtomicInteger(0),
        new AtomicInteger(0)
    };
    private final AtomicLong[] rpdResetDay = {
        new AtomicLong(0),
        new AtomicLong(0),
        new AtomicLong(0)
    };

    private final HttpClient  httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper    = new ObjectMapper();

    // ── Main entry point ─────────────────────────────────────────────────────
    // Throws IllegalArgumentException  → non-dental input   (controller → 400)
    // Throws RuntimeException          → all models exhausted (controller → 503)

    public SymptomAnalysis analyze(Long userId, SymptomRequest req) {
        String prompt  = buildPrompt(req);
        String rawJson = callGeminiWithSmartFallback(prompt);
        return parseResponse(userId, req, rawJson);
    }

    // ── Smart fallback: skip models that have hit their RPD limit ─────────────

    private String callGeminiWithSmartFallback(String prompt) {
        long todayEpochDay = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toEpochDay();
        int  baseDelayMs   = 2000;

        for (int i = 0; i < MODEL_URLS.length; i++) {
           
            if (rpdResetDay[i].get() != todayEpochDay) {
                rpdUsed[i].set(0);
                rpdResetDay[i].set(todayEpochDay);
            }

            if (rpdUsed[i].get() >= RPD_LIMITS[i]) {
                continue;
            }

            try {
                String result = attemptCall(MODEL_URLS[i], prompt, baseDelayMs);
                rpdUsed[i].incrementAndGet();
                return result;

            } catch (RateLimitException e) {
  
                int retryAfterMs = e.retryAfterSeconds > 0
                    ? e.retryAfterSeconds * 1000
                    : baseDelayMs;
                sleep(Math.min(retryAfterMs, 5000)); 
                continue;

            } catch (RuntimeException e) {
            
                if (i < MODEL_URLS.length - 1) {
                    sleep(baseDelayMs);
                    continue;
                }
                throw e;
            }
        }

        throw new RuntimeException(
            "AI analysis is currently unavailable. Please try again later, " +
            "or use the Browse by Service option to book directly."
        );
    }

    private String attemptCall(String url, String prompt, int delayMs) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(buildRequestBody(prompt)))
                .build();

            HttpResponse<String> response = httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
            );

            int status = response.statusCode();

            if (status == 429) {
           
                int retryAfter = response.headers()
                    .firstValue("Retry-After")
                    .map(v -> { try { return Integer.parseInt(v); } catch (Exception e) { return 0; } })
                    .orElse(0);
                throw new RateLimitException(retryAfter);
            }

            if (status == 503) {
                throw new RuntimeException("Model unavailable (503)");
            }

            if (status != 200) {
                throw new RuntimeException(
                    "AI analysis is currently unavailable. Please try again later, " +
                    "or use the Browse by Service option to book directly."
                );
            }

            JsonNode root = mapper.readTree(response.body());
            return root
                .path("candidates").get(0)
                .path("content")
                .path("parts").get(0)
                .path("text")
                .asText();

        } catch (RateLimitException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(
                "AI analysis is currently unavailable. Please try again later, " +
                "or use the Browse by Service option to book directly."
            );
        }
    }

    private String buildPrompt(SymptomRequest req) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are a dental triage assistant.\n\n");

        sb.append("STEP 1 — RELEVANCE CHECK:\n");
        sb.append("If input is unrelated to dental/oral health (teeth, gums, jaw, mouth), ");
        sb.append("respond ONLY with:\n");
        sb.append("{\"error\":\"non_dental\",\"message\":\"Hmm, that doesn't seem like a dental concern. ");
        sb.append("This assistant is only for dental and oral health symptoms. ");
        sb.append("Please describe a tooth, gum, jaw, or mouth-related issue — ");
        sb.append("or use the Browse by Service option to book directly.\"}\n\n");

        sb.append("STEP 2 — TRIAGE (dental input only):\n");
        sb.append("Respond ONLY with valid JSON, no markdown.\n\n");

        // Patient data — compact key: value format
        sb.append("Complaints: ").append(join(req.getChiefComplaints())).append("\n");
        if (req.getDescription() != null && !req.getDescription().isBlank())
            sb.append("Description: ").append(req.getDescription()).append("\n");
        sb.append("Pain: ").append(req.getHasPain()).append("\n");
        if ("Yes".equals(req.getHasPain())) {
            sb.append("Pain types: ").append(join(req.getPainTypes())).append("\n");
            sb.append("Severity: ").append(req.getPainLevel()).append("\n");
            sb.append("Biting pain: ").append(req.getBitingPain()).append("\n");
        }
        sb.append("Sensitivity: ").append(join(req.getSensitivityTriggers())).append("\n");
        if (req.getSensitivityDuration() != null)
            sb.append("Sensitivity duration: ").append(req.getSensitivityDuration()).append("\n");
        sb.append("Started: ").append(req.getDuration()).append("\n");
        sb.append("Progress: ").append(req.getProgress()).append("\n");
        sb.append("Medication: ").append(req.getTookMedication()).append("\n");
        if ("Yes".equals(req.getTookMedication())) {
            sb.append("Med helped: ").append(req.getMedicationHelped()).append("\n");
            sb.append("Med name: ").append(req.getMedicationName()).append("\n");
        }
        sb.append("History: ").append(join(req.getDentalHistory())).append("\n\n");

        // Required output schema
        sb.append("Respond with:\n");
        sb.append("{\"aiAnalysis\":\"2-3 sentence clinical summary\",");
        sb.append("\"suggestedProcedures\":\"comma-separated procedures\",");
        sb.append("\"urgencyLevel\":\"emergency|urgent|moderate|routine\",");
        sb.append("\"estimatedDurationMin\":60,");
        sb.append("\"recommendedSpecialization\":\"general|orthodontics|endodontics|prosthodontics|periodontics|cosmetic\"}\n\n");

        sb.append("Urgency: emergency=severe pain/swelling/abscess, urgent=significant pain/broken tooth ");
        sb.append("moderate=persistent discomfort/sensitivity, routine=mild or no pain");

        return sb.toString();
    }

    private String buildRequestBody(String prompt) throws Exception {
        return mapper.writeValueAsString(
            mapper.createObjectNode()
                .set("contents", mapper.createArrayNode()
                    .add(mapper.createObjectNode()
                        .set("parts", mapper.createArrayNode()
                            .add(mapper.createObjectNode()
                                .put("text", prompt)))))
        );
    }

    private SymptomAnalysis parseResponse(Long userId, SymptomRequest req, String rawText) {

        String clean = rawText
            .replaceAll("(?s)```json\\s*", "")
            .replaceAll("(?s)```\\s*", "")
            .trim();

        if (clean.contains("\"non_dental\"")) {
            try {
                JsonNode j = mapper.readTree(extractFirstJson(clean));
                if (j.has("error") && "non_dental".equals(j.path("error").asText())) {
                    throw new IllegalArgumentException(j.path("message").asText(defaultNonDentalMsg()));
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception ignored) {
                throw new IllegalArgumentException(defaultNonDentalMsg());
            }
        }

        JsonNode json;
        try {
            json = mapper.readTree(extractFirstJson(clean));
        } catch (Exception e) {
            throw new RuntimeException(
                "AI analysis is currently unavailable. Please try again later, " +
                "or use the Browse by Service option to book directly."
            );
        }

        if (json.has("error") && "non_dental".equals(json.path("error").asText())) {
            throw new IllegalArgumentException(json.path("message").asText(defaultNonDentalMsg()));
        }

        SymptomAnalysis analysis = new SymptomAnalysis();
        analysis.setUserInfoId(userId);
        analysis.setSymptomDescription(buildReadableSummary(req));
        analysis.setAiAnalysis(json.path("aiAnalysis").asText("Unable to analyze symptoms."));
        analysis.setSuggestedProcedures(json.path("suggestedProcedures").asText("Consultation"));
        analysis.setUrgencyLevel(json.path("urgencyLevel").asText("routine"));
        analysis.setEstimatedDurationMin(json.path("estimatedDurationMin").asInt(60));
        analysis.setRecommendedSpecialization(json.path("recommendedSpecialization").asText("General Dentistry"));
        analysis.setAnalyzedAt(LocalDateTime.now());

        return analysis;
    }

    private String extractFirstJson(String text) {
        int start = text.indexOf('{');
        int end   = text.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start)
            return text.substring(start, end + 1);
        return text;
    }

    private String join(java.util.List<String> list) {
        if (list == null || list.isEmpty()) return "None";
        return String.join(", ", list);
    }

    private String defaultNonDentalMsg() {
        return "Hmm, that doesn't seem like a dental concern. " +
               "This assistant is only for dental and oral health symptoms. " +
               "Please describe a tooth, gum, jaw, or mouth-related issue — " +
               "or use the Browse by Service option to book directly.";
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private String buildReadableSummary(SymptomRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Complaints: ").append(join(req.getChiefComplaints())).append(". ");
        if (req.getDescription() != null && !req.getDescription().isBlank())
            sb.append("Patient says: ").append(req.getDescription()).append(". ");
        sb.append("Pain: ").append(req.getHasPain());
        if ("Yes".equals(req.getHasPain()))
            sb.append(" — ").append(join(req.getPainTypes()))
              .append(", ").append(req.getPainLevel()).append(" severity");
        sb.append(". Sensitivity: ").append(join(req.getSensitivityTriggers()));
        sb.append(". Duration: ").append(req.getDuration());
        sb.append(". Progress: ").append(req.getProgress()).append(".");
        return sb.toString();
    }

    private static class RateLimitException extends RuntimeException {
        final int retryAfterSeconds;
        RateLimitException(int retryAfterSeconds) {
            super("Rate limited");
            this.retryAfterSeconds = retryAfterSeconds;
        }
    }
}
