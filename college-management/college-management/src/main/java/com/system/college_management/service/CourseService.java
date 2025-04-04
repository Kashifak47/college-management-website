package com.system.college_management.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.college_management.entity.Course;
import com.system.college_management.repository.CourseRepository;

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;
	
	public List<Course> getAllCourses(){
		return courseRepository.findAll();
	}
	public Optional<Course> getCourseById(int id) {
		return courseRepository.findById(id);
	}
	public Course saveCourse(Course course) {
		return courseRepository.save(course);
	}
	public void deleteCourse(int id) {
		courseRepository.deleteById(id);
	}
}
