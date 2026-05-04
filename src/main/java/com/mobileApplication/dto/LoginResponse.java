package com.mobileApplication.dto;

public record LoginResponse (String status, String token, UserInfo user) {

}
