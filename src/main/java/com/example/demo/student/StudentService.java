package com.example.demo.student;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudent() {
        return studentRepository.findAll();
    }

    public Student Create(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("Email already taken");
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists) {
            throw new IllegalStateException("Student with id " + studentId + " does not exist");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional // se ce un errore fa un roolback di tutto e non fa il commit
    public void updateStudent(long id, Student student) {
        if (studentRepository.existsById(id)) {
            studentRepository.findById(id).ifPresent(s -> {
                if (student.getName() != null && student.getName().length() > 0
                        && !student.getName().equals(s.getName())) {
                    s.setName(student.getName());
                }
                if (student.getEmail() != null && student.getEmail().length() > 0
                        && !student.getEmail().equals(s.getEmail())) {
                    if (studentRepository.findStudentByEmail(student.getEmail()).isPresent()) {
                        throw new IllegalStateException("Email already taken");
                    } else {
                        s.setEmail(student.getEmail());
                    }
                }
                if (student.getDob() != null && !student.getDob().equals(s.getDob())) {
                    s.setDob(student.getDob());
                }
                studentRepository.save(s);
            });
        } else {
            throw new IllegalStateException("Student with id " + id + " does not exist");

        }
    }
}
