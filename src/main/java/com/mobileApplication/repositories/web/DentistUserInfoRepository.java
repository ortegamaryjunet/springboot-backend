package com.mobileApplication.repositories.web;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobileApplication.models.UserInfo;

@Repository
public interface DentistUserInfoRepository extends JpaRepository<UserInfo, Long> {
	
	List<UserInfo> findAllByIdIn(List<Long> ids);

}
