package com.mobileApplication.dto;

import java.util.List;

public record EmailRequest (String from, List<String> to, String subject, String html) {

}
