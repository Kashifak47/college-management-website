package com.system.college_management.controller;

import java.io.File;
import java.io.File.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.system.college_management.entity.Course;
import com.system.college_management.entity.Student;
import com.system.college_management.entity.Teacher;
import com.system.college_management.service.AdminService;
import com.system.college_management.service.CourseService;
import com.system.college_management.service.StudentService;
import com.system.college_management.service.TeacherService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@SessionAttributes("adminEmail")
public class AdminController {

	@Autowired
	private AdminService adminService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService; 	
	@Autowired
	private CourseService courseService;
	
    @GetMapping("/login")
    public String showAdminLoginPage() {
        return "admin-login";
    }
    @GetMapping("/index")
    public String backToHomePage() {
    	return "redirect:/";
    }

    @PostMapping("/login")
    public String loginAdmin(@RequestParam String email, @RequestParam String password, Model model) {
    	boolean isAuthenticated = adminService.authenticateAdmin(email, password);	
    	if(isAuthenticated) {
    		model.addAttribute("adminEmail", email);
    		return "redirect:/admin/dashboard";
    	}else {
    		model.addAttribute("error", "Invalid email or password!");
    		return "admin-login";
    	}
    }
    @GetMapping("/dashboard")
    public String showAdminDashboard(Model model) {
    	if(!model.containsAttribute("adminEmail")) {
    		return "redirect:/admin/login";
    	}
    	return "admin-dashboard";
    }
    @GetMapping("/logout")
    public String logoutAdmin(SessionStatus status, RedirectAttributes redirectAttributes) {
        status.setComplete(); 
        redirectAttributes.addFlashAttribute("logoutMessage", "You have successfully logged out!");
        return "redirect:/login"; 
    }
    
//    @GetMapping("/students")
//    public String manageStudents(Model model) {
//        if (!model.containsAttribute("adminEmail")) {
//            return "redirect:/login";
//        }
//        model.addAttribute("courses", courseService.getAllCourses());
//        model.addAttribute("students", studentService.getAllStudents());
//        model.addAttribute("student", new Student()); 
//        return "students";
//    }
//
//    @PostMapping("/student/save")
//    public String saveStudent(@ModelAttribute Student student) {
//        studentService.saveStudent(student);
//        return "redirect:/admin/students";
//    }
//
//    @GetMapping("/student/edit/{id}")
//    public String editStudent(@PathVariable int id, Model model) {
//        if (!model.containsAttribute("adminEmail")) {
//            return "redirect:/admin/login";
//        }
//        Optional<Student> student = studentService.getStudentById(id);
//        List<Course> courses = courseService.getAllCourses();
//        model.addAttribute("students", studentService.getAllStudents());
//        model.addAttribute("student", student.orElse(new Student())); 
//        model.addAttribute("courses", courses);
//        return "students"; 
//    }
//
//    @GetMapping("/student/delete/{id}")
//    public String deleteStudent(@PathVariable int id) {
//        studentService.deleteStudent(id);
//        return "redirect:/admin/students";
//    }
    
    
 // Manage Students Page
    @GetMapping("/students")
    public String manageStudents(Model model, HttpSession session) {
        if (session.getAttribute("adminEmail") == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("student", new Student()); // Empty student object for form
        return "students";
    }
    
    
    @PostMapping("/student/update/{id}")
    public String updateStudent(@PathVariable int id, 
                                @ModelAttribute Student student, 
                                @RequestParam MultipartFile imageFile, 
                                HttpSession session) {
        // 🛑 Check if admin is logged in
        if (session.getAttribute("adminEmail") == null) {
            return "redirect:/admin/login";
        }

        try {
            String uploadDir = "uploads/";
            File uploadPath = new File(uploadDir);
            if (!uploadPath.exists()) {
                uploadPath.mkdirs();
            }

            Optional<Student> existingStudentOpt = studentService.getStudentById(id);
            if (!existingStudentOpt.isPresent()) {
                return "redirect:/admin/students?error=Student+not+found";
            }

            Student existingStudent = existingStudentOpt.get();

            // If a new image is uploaded, delete the old one and save the new one
            if (imageFile != null && !imageFile.isEmpty()) {
                // Delete old image if it exists and is not "default.png"
                if (existingStudent.getImageFileName() != null 
                    && !existingStudent.getImageFileName().equals("default.png")) {
                    File oldImage = new File(uploadDir + existingStudent.getImageFileName());
                    if (oldImage.exists()) {
                        oldImage.delete(); // Delete old image
                    }
                }

                // Save new image
                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                student.setImageFileName(fileName);
            } else {
                // No new image uploaded, keep old image
                student.setImageFileName(existingStudent.getImageFileName());
            }

            student.setId(id); // Ensure existing student ID is maintained
            studentService.saveStudent(student);
            return "redirect:/admin/students?success=Student+updated+successfully";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }


    // Save New or Edited Student
//    @PostMapping("/student/save")
//    public String saveStudent(@ModelAttribute Student student, @RequestParam("imageFile") MultipartFile imageFile) {
//    	try {
//            if (!imageFile.isEmpty()) {
//               
//                String uploadDir = "uploads/";
//                File uploadPath = new File(uploadDir);
//                if (!uploadPath.exists()) {
//                    uploadPath.mkdirs(); // Create directory if it doesn't exist
//                }
//
//                // Generate file path
//                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename(); // Unique name
//                Path filePath = Paths.get(uploadDir).resolve(fileName);
//
//                // Save file
//                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//                // Save file path in Student entity
//                student.setImageFile(uploadDir + fileName);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            return "students";
//        }
//        studentService.saveStudent(student);
//        return "redirect:/admin/students?success=Student+saved+successfully";
//    }

    @PostMapping("/student/save")
    public String saveStudent(@ModelAttribute Student student, @RequestParam MultipartFile imageFile, Model model) {
        try {
            if (!imageFile.isEmpty() && imageFile != null) {
                // New image uploaded, replace existing
                String uploadDir = "uploads/";
                File uploadPath = new File(uploadDir);
                if (!uploadPath.exists()) {
                    uploadPath.mkdirs();
                }

                String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                Path filePath = Paths.get(uploadDir, fileName);

                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                student.setImageFileName(fileName);
            } else {
                // No new image uploaded
                if (student.getId() == 0) {
                    // New student, no image uploaded, assign default
                    student.setImageFileName("default.png");
                } else {
                    // Existing student, no new image, keep old image
                    // Do nothing, imageFileName remains the same.
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        studentService.saveStudent(student);
        return "redirect:/admin/students?success=Student+saved+successfully";
    }
    
    // Edit Student
    @GetMapping("/student/edit/{id}")
    public String editStudent(@PathVariable int id, Model model, HttpSession session) {
        if (session.getAttribute("adminEmail") == null) {
            return "redirect:/admin/login";
        }

        Optional<Student> studentOptional = studentService.getStudentById(id);
        if (!studentOptional.isPresent()) {
            return "redirect:/admin/students?error=Student+not+found";
        }
        

        model.addAttribute("student", studentOptional.get());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "students"; // Reload the same page with form data
    }

    // Delete Student
//    @GetMapping("/student/delete/{id}")
//    public String deleteStudent(@PathVariable int id) {
//        if (studentService.getStudentById(id).isPresent()) {
//            studentService.deleteStudent(id);
//            return "redirect:/admin/students?success=Student+deleted+successfully";
//        } else {
//            return "redirect:/admin/students?error=Student+not+found";
//        }
//    }
    
    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        Optional<Student> studentOptional = studentService.getStudentById(id);
        
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            
            // Get image filename
            String imageFileName = student.getImageFileName();
            
            // Check if the image exists and delete it
            if (imageFileName != null && !imageFileName.isEmpty()) {
                String imagePath = "uploads/" + imageFileName; // Relative path
                File imageFile = new File(imagePath);
                
                if (imageFile.exists()) {
                    imageFile.delete(); // Delete the file
                }
            }
            
            // Delete the student from the database
            studentService.deleteStudent(id);
            
            return "redirect:/admin/students?success=Student+deleted+successfully";
        } else {
            return "redirect:/admin/students?error=Student+not+found";
        }
    }

    
    
    @GetMapping("/teachers")
    public String manageTeachers(Model model) {
        if (!model.containsAttribute("adminEmail")) {
            return "redirect:/admin/login";
        }
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("courses", courses);
        return "teachers";
    }

    @PostMapping("/teacher/save")
    public String saveTeacher(@ModelAttribute Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/admin/teachers";
    }

    @GetMapping("/teacher/edit/{id}")
    public String editTeacher(@PathVariable int id, Model model) {
        if (!model.containsAttribute("adminEmail")) {
            return "redirect:/admin/login";
        }
        Optional<Teacher> teacher = teacherService.getTeacherById(id);
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("teachers", teacherService.getAllTeachers());
        model.addAttribute("teacher", teacher.orElse(new Teacher()));
        model.addAttribute("courses", courses);
        return "teachers";
    }

    @GetMapping("/teacher/delete/{id}")
    public String deleteTeacher(@PathVariable int id) {
        teacherService.deleteTeacher(id);
        return "redirect:/admin/teachers";
    }

    @GetMapping("/courses")
    public String manageCourse(Model model) {
        if (!model.containsAttribute("adminEmail")) {
            return "redirect:/admin/login";
        }
        model.addAttribute("courses", courseService.getAllCourses()); // Fetch courses
        model.addAttribute("course", new Course()); // Empty object for adding/editing
        return "courses";
    }

    // Save (Add or Update Course)
    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/admin/courses";
    }

    // Edit Course (Load data for editing)
    @GetMapping("/course/edit/{id}")
    public String editCourse(@PathVariable int id, Model model) {
        if (!model.containsAttribute("adminEmail")) {
            return "redirect:/admin/login";
        }
        Optional<Course> course = courseService.getCourseById(id);
        model.addAttribute("courses", courseService.getAllCourses()); // Refresh list
        model.addAttribute("course", course.orElse(new Course())); // Load course
        return "courses"; // Reload same page
    }

    // Delete Course
    @GetMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return "redirect:/admin/courses";
    }

}
