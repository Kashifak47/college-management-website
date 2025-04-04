package com.system.college_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.system.college_management.entity.Student;
import com.system.college_management.service.StudentService;

@Controller
@RequestMapping("/student")
@SessionAttributes("studentEmail")
public class StudentController {

	@Autowired
	private StudentService studentService;
	
	@GetMapping("/login")
	public String studentLoginPage() {
		return "student-login";
	}
	
	@PostMapping("/login")
	public String loginStudent(@RequestParam String email, @RequestParam String password, Model model) {
		boolean isAuthenticated = studentService.authenticateStudent(email, password);
		if(isAuthenticated) {
			model.addAttribute("studentEmail", email);
			return "redirect:/student/dashboard";
		}else {
			model.addAttribute("error", "Invalid username or password");
			return "student-login";
		}
	}
	
	@GetMapping("/dashboard")
	public String showStudentDashboard(@ModelAttribute("studentEmail") String email, Model model) {
		if(email == null || email.isEmpty()) {
			return "redirect:/student/login";
		}
		
		Student student = studentService.getStudentByEmail(email);
		if(student==null) {
			return "redirect:/student/login";
		}
		model.addAttribute("student", student);
		return "student-dashboard";
	}
	
	@GetMapping("/logout")
	public String logoutStudent(SessionStatus status) {
		status.setComplete();
		return "redirect:/login";
	}
	
}
