package com.system.college_management.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="courses")
public class Course {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false, length = 100)
	private String courseName;
	@Column(nullable = false, length = 100)
	private String department;
	@Column(nullable = false)
	private int duration;
	@OneToMany(mappedBy="course", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Student> students;
	@OneToMany(mappedBy="course", cascade=CascadeType.ALL, orphanRemoval=true)
	private List<Teacher> teachers;
	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Course(int id, String courseName, String department, int duration, List<Student> students,
			List<Teacher> teachers) {
		this.id = id;
		this.courseName = courseName;
		this.department = department;
		this.duration = duration;
		this.students = students;
		this.teachers = teachers;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}
	public List<Teacher> getTeachers() {
		return teachers;
	}
	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
	
	
	
}
