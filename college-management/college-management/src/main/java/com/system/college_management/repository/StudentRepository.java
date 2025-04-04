package com.system.college_management.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.system.college_management.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

	Student findByEmail(String email);
	
	 // Find student by roll number (unique)
    Optional<Student> findByRollNumber(String rollNumber);

    // Find students by gender
    List<Student> findByGender(String gender);

    // Find students by category (General, OBC, SC, ST, etc.)
    List<Student> findByCategory(String category);

    // Find students by city
    List<Student> findByCity(String city);

    // Find students by state
    List<Student> findByState(String state);

    // Find students by country
    List<Student> findByCountry(String country);

    // Find students by father's name (useful for searching in case of duplicate names)
    List<Student> findByFatherName(String fatherName);

    // Find students by fee status
    List<Student> findByFeeStatus(String feeStatus);


}
