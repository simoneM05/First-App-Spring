package com.example.demo.student;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long/* perche l'id e di tipo long nella classe */ > {

    @Query("SELECT s FROM Student s WHERE s.email = ?1") // ?1 e il primo parametro
    Optional<Student> findStudentByEmail(String email);
}
