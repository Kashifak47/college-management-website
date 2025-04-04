package com.system.college_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.system.college_management.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Admin findByEmail(String email);



}
