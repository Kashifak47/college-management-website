package com.system.college_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.system.college_management.entity.Teacher;
import com.system.college_management.service.TeacherService;

@Controller
@RequestMapping("/teacher")
@SessionAttributes("teacherEmail")
public class TeacherController {

	@Autowired
	private TeacherService teacherService;
	
	public String showTeacherLoginPage() {
		return "teacher-login";
	}
	
	@PostMapping("/login")
	public String loginTeacher(@RequestParam String email, @RequestParam String password, Model model) {
		boolean isAuthenticated = teacherService.authenticateTeacher(email, password);
		if(isAuthenticated) {
			model.addAttribute("teacherEmail", email);
			return "redirect:/teacher/dashboard";
		}else {
			model.addAttribute("error", "Invalid email or password");
			return "teacher-login";
		}
	}
	
	@GetMapping("/dashboard")
	public String showTeacherDashboard(@SessionAttribute(value = "teacherEmail", required = false) String email, Model model) {
	    if (email == null || email.isEmpty()) {
	        return "redirect:/teacher/login";
	    }

	    Teacher teacher = teacherService.getTeacherByEmail(email);
	    if (teacher == null) {
	        return "redirect:/teacher/login";
	    }

	    model.addAttribute("teacher", teacher);
	    return "teacher-dashboard";
	}
}
