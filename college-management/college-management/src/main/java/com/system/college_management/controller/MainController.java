package com.system.college_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.system.college_management.service.AdminService;
import com.system.college_management.service.StudentService;
import com.system.college_management.service.TeacherService;

@Controller
@SessionAttributes({"adminEmail", "teacherEmail", "studentEmail"})
public class MainController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String homePage() {
        return "index";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String role,
            @RequestParam String email,
            @RequestParam String password,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        boolean isAuthenticated = false;
        String redirectUrl = "redirect:/login"; // Default redirect on failure

        switch (role) {
            case "admin":
                isAuthenticated = adminService.authenticateAdmin(email, password);
                if (isAuthenticated) {
                    model.addAttribute("adminEmail", email);
                    redirectUrl = "redirect:/admin/dashboard";
                }
                break;
            case "teacher":
                isAuthenticated = teacherService.authenticateTeacher(email, password);
                if (isAuthenticated) {
                    model.addAttribute("teacherEmail", email);
                    redirectUrl = "redirect:/teacher/dashboard";
                }
                break;
            case "student":
                isAuthenticated = studentService.authenticateStudent(email, password);
                if (isAuthenticated) {
                    model.addAttribute("studentEmail", email);
                    redirectUrl = "redirect:/student/dashboard";
                }
                break;
            default:
                redirectAttributes.addFlashAttribute("error", "Invalid role selected.");
                return "redirect:/login";
        }

        if (!isAuthenticated) {
            redirectAttributes.addFlashAttribute("error", "Invalid email or password! or Check role");
        }

        return redirectUrl;
    }
}
