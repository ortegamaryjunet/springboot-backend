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
import com.mobileApplication.dto.RiskAssessmentRequest;
import com.mobileApplication.models.RiskAssessment;

@Service
public class GeminiRiskService {

    @Value("${gemini.risk.api.key}")
    private String apiKey;

    // Same model cascade as GeminiService — lite first, then flash, then pro
    private static final String[] MODEL_URLS = {
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-lite:generateContent",
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
    };

    private static final int[] RPD_LIMITS = { 1500, 500 };
    private final AtomicInteger[] rpdUsed = {
        new AtomicInteger(0),
        new AtomicInteger(0)
    };
    private final AtomicLong[] rpdResetDay = {
        new AtomicLong(0),
        new AtomicLong(0)
    };

    private final HttpClient   httpClient = HttpClient.newHttpClient();
    private final ObjectMapper mapper     = new ObjectMapper();

    // ── Main entry point ─────────────────────────────────────────────────────
    // Throws RuntimeException → all models exhausted (controller → 503)

    public RiskAssessment assess(Long userId, RiskAssessmentRequest req) {
        String prompt  = buildPrompt(req);
        String rawJson = callGeminiWithSmartFallback(prompt);
        return parseResponse(userId, req, rawJson);
    }

    // ── Smart fallback: mirrors GeminiService exactly ─────────────────────────

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
            "Risk assessment is temporarily unavailable. Please try again later."
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
                throw new RuntimeException("Risk assessment is temporarily unavailable. Please try again later.");
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
            throw new RuntimeException("Risk assessment is temporarily unavailable. Please try again later.");
        }
    }

    // ── Prompt ────────────────────────────────────────────────────────────────

    private String buildPrompt(RiskAssessmentRequest req) {
        StringBuilder sb = new StringBuilder();

        sb.append("You are a dental health risk predictor.\n\n");

        sb.append("Based on the patient's self-reported dental habits and health history below, ");
        sb.append("predict their risk of developing dental conditions in the future.\n\n");

        sb.append("PATIENT DATA:\n");
        sb.append("Current symptoms: ").append(joinList(req.getCurrentSymptoms(), req.isSymptomsNone())).append("\n");
        sb.append("Brushing frequency: ").append(nullSafe(req.getBrushFrequency())).append("\n");
        sb.append("Last dental visit: ").append(nullSafe(req.getLastDentalVisit())).append("\n");
        sb.append("Daily habits: ").append(joinList(req.getDailyHabits(), req.isHabitsNone())).append("\n");
        sb.append("Health conditions: ").append(joinList(req.getHealthConditions(), req.isConditionsNone())).append("\n");
        sb.append("Flossing: ").append(nullSafe(req.getFlossFrequency())).append("\n");
        sb.append("Mouthwash use: ").append(nullSafe(req.getMouthwashFrequency())).append("\n");
        sb.append("Smoking/tobacco: ").append(nullSafe(req.getSmokingStatus())).append("\n");

        if (req.getAdditionalNotes() != null && !req.getAdditionalNotes().isBlank()) {
            sb.append("Additional notes (IMPORTANT — only consider this if it describes a dental ")
              .append("symptom, concern, or oral health history; if it is unrelated to dental health, ")
              .append("ignore it completely and do not factor it into the assessment): ")
              .append(req.getAdditionalNotes()).append("\n");
        }

        sb.append("\n");

        sb.append("Respond ONLY with valid JSON, no markdown, no explanation outside the JSON.\n\n");

        sb.append("Required format:\n");
        sb.append("{\n");
        sb.append("  \"riskLevel\": \"low|moderate|high\",\n");
        sb.append("  \"riskSummary\": \"2-3 sentence plain-English explanation of the patient's overall risk\",\n");
        sb.append("  \"predictedConditions\": \"comma-separated list of dental conditions this patient is at risk of developing\",\n");
        sb.append("  \"contributingFactors\": \"comma-separated list of the specific habits or conditions driving their risk\",\n");
        sb.append("  \"recommendedServices\": \"comma-separated dental services the patient should book (e.g. Dental Cleaning, Periodontal Screening, Fluoride Treatment)\",\n");
        sb.append("  \"preventionTips\": \"3-4 concise, actionable tips to reduce their risk\"\n");
        sb.append("}\n\n");

        sb.append("Risk level guidelines:\n");
        sb.append("- low: good hygiene habits, no concerning symptoms, regular visits\n");
        sb.append("- moderate: some risk factors present (infrequent brushing/flossing, occasional sugary diet, overdue visit)\n");
        sb.append("- high: multiple risk factors, active symptoms, systemic conditions like diabetes, smoking, or 3+ years without a dental visit\n");

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

    // ── Parse ─────────────────────────────────────────────────────────────────

    private RiskAssessment parseResponse(Long userId, RiskAssessmentRequest req, String rawText) {

        String clean = rawText
            .replaceAll("(?s)```json\\s*", "")
            .replaceAll("(?s)```\\s*", "")
            .trim();

        JsonNode json;
        try {
            json = mapper.readTree(extractFirstJson(clean));
        } catch (Exception e) {
            throw new RuntimeException("Risk assessment is temporarily unavailable. Please try again later.");
        }

        RiskAssessment assessment = new RiskAssessment();
        assessment.setUserInfoId(userId);
        assessment.setInputSummary(buildInputSummary(req));
        assessment.setRiskLevel(json.path("riskLevel").asText("moderate"));
        assessment.setRiskSummary(json.path("riskSummary").asText("Unable to generate risk summary."));
        assessment.setPredictedConditions(json.path("predictedConditions").asText(""));
        assessment.setContributingFactors(json.path("contributingFactors").asText(""));
        assessment.setRecommendedServices(json.path("recommendedServices").asText("Dental Consultation"));
        assessment.setPreventionTips(json.path("preventionTips").asText(""));
        assessment.setAssessedAt(LocalDateTime.now());

        return assessment;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private String extractFirstJson(String text) {
        int start = text.indexOf('{');
        int end   = text.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start)
            return text.substring(start, end + 1);
        return text;
    }

    private String joinList(java.util.List<String> list, boolean noneSelected) {
        if (noneSelected) return "None";
        if (list == null || list.isEmpty()) return "Not specified";
        return String.join(", ", list);
    }

    private String nullSafe(String value) {
        return (value == null || value.isBlank()) ? "Not specified" : value;
    }

    private String buildInputSummary(RiskAssessmentRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append("Symptoms: ").append(joinList(req.getCurrentSymptoms(), req.isSymptomsNone())).append(". ");
        sb.append("Brushing: ").append(nullSafe(req.getBrushFrequency())).append(". ");
        sb.append("Last visit: ").append(nullSafe(req.getLastDentalVisit())).append(". ");
        sb.append("Habits: ").append(joinList(req.getDailyHabits(), req.isHabitsNone())).append(". ");
        sb.append("Conditions: ").append(joinList(req.getHealthConditions(), req.isConditionsNone())).append(". ");
        sb.append("Floss: ").append(nullSafe(req.getFlossFrequency())).append(". ");
        sb.append("Mouthwash: ").append(nullSafe(req.getMouthwashFrequency())).append(". ");
        sb.append("Smoking: ").append(nullSafe(req.getSmokingStatus())).append(".");
        if (req.getAdditionalNotes() != null && !req.getAdditionalNotes().isBlank())
            sb.append(" Notes: ").append(req.getAdditionalNotes());
        return sb.toString();
    }

    private void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    private static class RateLimitException extends RuntimeException {
        final int retryAfterSeconds;
        RateLimitException(int retryAfterSeconds) {
            super("Rate limited");
            this.retryAfterSeconds = retryAfterSeconds;
        }
    }
    
}
