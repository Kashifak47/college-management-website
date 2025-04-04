package com.system.college_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.college_management.entity.Teacher;
import com.system.college_management.repository.TeacherRepository;

@Service
public class TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;
	
	public List<Teacher> getAllTeachers(){
		return teacherRepository.findAll();
	}
	
	public Optional<Teacher> getTeacherById(int id) {
		return teacherRepository.findById(id);
	}
	
	public Teacher saveTeacher(Teacher teacher) {
		return teacherRepository.save(teacher);
	}
	
	public void deleteTeacher(int id) {
		teacherRepository.deleteById(id);
	}
	
	public boolean authenticateTeacher(String email, String password) {
		Teacher teacher = teacherRepository.findByEmail(email);
		return teacher != null && teacher.getPassword().equals(password);
	}

	public Teacher getTeacherByEmail(String email) {
		// TODO Auto-generated method stub
		return teacherRepository.findByEmail(email);
	}
}
