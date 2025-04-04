package com.system.college_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.college_management.entity.Student;
import com.system.college_management.entity.Teacher;
import com.system.college_management.repository.StudentRepository;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	public List<Student> getAllStudents(){
		return studentRepository.findAll();
	}
	
	public Optional<Student> getStudentById(int id) {
		return studentRepository.findById(id);
	}
	public Student getStudentByEmail(String email) {
	    return studentRepository.findByEmail(email);
	}
	
	public Student saveStudent(Student student) {
		return studentRepository.save(student);
	}
	public void deleteStudent(int id) {
		 studentRepository.deleteById(id);
	}
	public boolean authenticateStudent(String email, String password) {
		Student student = studentRepository.findByEmail(email);
		return student != null && student.getPassword().equals(password);
	}
}
