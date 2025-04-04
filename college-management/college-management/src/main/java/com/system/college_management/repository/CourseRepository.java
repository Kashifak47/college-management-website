package com.system.college_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.college_management.entity.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

	void deleteById(Integer id);

}
