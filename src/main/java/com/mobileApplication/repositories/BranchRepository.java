package com.mobileApplication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobileApplication.models.BranchModel;

public interface BranchRepository extends JpaRepository<BranchModel, Long> {

}
