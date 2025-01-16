package com.shahed.SpringSecEx.Repository;

import com.shahed.SpringSecEx.model.Student;
import com.shahed.SpringSecEx.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
