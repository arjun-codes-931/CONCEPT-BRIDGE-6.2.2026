package com.concept.conceptbridge.admin;

import com.concept.conceptbridge.entity.User;
import com.concept.conceptbridge.repository.UserRepository;
import com.concept.conceptbridge.student.*;
import com.concept.conceptbridge.teacher.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentProfileRepository studentRepo;

    @Autowired
    private TeacherProfileRepository teacherRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;


    // =====================
    // STUDENT FUNCTIONS
    // =====================

//    public StudentProfile createStudent(String username,String password,
//                                        String roll,String reg,String branch,
//                                        Integer semester,String batch,
//                                        String section,Integer year,
//                                        String college,String university) {
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password); // no encoding
//        user.setRole(Role.STUDENT);
//
//        User savedUser = userRepository.save(user);
//
//        StudentProfile profile = new StudentProfile(
//                savedUser,
//                roll,
//                reg,
//                branch,
//                semester,
//                batch,
//                section,
//                year,
//                college,
//                university
//        );
//
//        return studentRepo.save(profile);
//    }


    public List<StudentProfile> getAllStudents(){
        return studentRepo.findAll();
    }


    public void deactivateStudent(Long id){

        StudentProfile profile = studentRepo.findById(id)
                .orElseThrow();

        profile.deactivate();

        studentRepo.save(profile);
    }



    // =====================
    // TEACHER FUNCTIONS
    // =====================

//    public TeacherProfile createTeacher(String username,String password,
//                                        String name,String empId,String dept){
//
//        User user = new User();
//        user.setUsername(username);
//        user.setPassword(password); // no encoding
//        user.setRole(Role.TEACHER);
//
//        User savedUser = userRepository.save(user);
//
//        TeacherProfile teacher =
//                new TeacherProfile(savedUser,name,empId,dept);
//
//        return teacherRepo.save(teacher);
//    }


    public List<TeacherProfile> getAllTeachers(){
        return teacherRepo.findAll();
    }


    public void deleteTeacher(Long id){

        teacherRepo.deleteById(id);
    }

}