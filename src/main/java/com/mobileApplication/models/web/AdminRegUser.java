package com.mobileApplication.models.web;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "reg_users_tbl")
public class AdminRegUser {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry")
    private Long otpExpiry;

    @Column(name = "resend_count")
    private int resendCount = 0;

    @Column(name = "last_otp_sent")
    private Long lastOtpSent;

    @Column(name = "is_verified", nullable = false)
    private boolean verified = false;

    public AdminRegUser() {}

    public AdminRegUser(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getOtp() {
        return otp;
    }

    public Long getOtpExpiry() {
        return otpExpiry;
    }

    public int getResendCount() {
        return resendCount;
    }

    public Long getLastOtpSent() {
        return lastOtpSent;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public void setOtpExpiry(Long otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public void setResendCount(int resendCount) {
        this.resendCount = resendCount;
    }

    public void setLastOtpSent(Long lastOtpSent) {
        this.lastOtpSent = lastOtpSent;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
