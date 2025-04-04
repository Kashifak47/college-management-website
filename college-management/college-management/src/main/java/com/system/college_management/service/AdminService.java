package com.system.college_management.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.college_management.entity.Admin;
import com.system.college_management.repository.AdminRepository;

@Service
public class AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	
	public AdminService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}
	public Admin getAdminByEmail(String email) {
		return adminRepository.findByEmail(email);
	}
	public Admin saveAdmin(Admin admin) {
		return adminRepository.save(admin);
	}
	
	public boolean authenticateAdmin(String email, String password) {
		Admin admin = adminRepository.findByEmail(email);
		return admin!=null && admin.getPassword().equals(password);
	}
}
