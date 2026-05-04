package com.mobileApplication.services.web;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminResendEmailService {
	
	@Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String resendFrom;

    public void sendOtpEmail(String to, String otp, String purpose) {
        try {
            String subject = purpose.equals("REGISTER")
                    ? "Admin Registration OTP"
                    : "Password Reset OTP";

            String html = """
                    <h2>Dental Clinic Verification Code</h2>
                    <p>Your OTP code is:</p>
                    <h1>%s</h1>
                    <p>This code will expire in 5 minutes.</p>
                    """.formatted(otp);

            String cleanHtml = html
                    .replace("\n", "")
                    .replace("\r", "")
                    .replace("\"", "\\\"");

            String json = "{"
                    + "\"from\":\"" + resendFrom + "\","
                    + "\"to\":[\"" + to + "\"],"
                    + "\"subject\":\"" + subject + "\","
                    + "\"html\":\"" + cleanHtml + "\""
                    + "}";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("RESEND STATUS: " + response.statusCode());
            System.out.println("RESEND BODY: " + response.body());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new RuntimeException("Resend failed: " + response.body());
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

}
