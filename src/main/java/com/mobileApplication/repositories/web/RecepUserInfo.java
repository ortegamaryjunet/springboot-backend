package com.mobileApplication.repositories.web;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.UserInfo;

public interface RecepUserInfo extends JpaRepository<UserInfo, Long> {

}
