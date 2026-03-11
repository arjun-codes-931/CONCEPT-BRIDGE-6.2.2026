package com.concept.conceptbridge.student;

import com.concept.conceptbridge.entity.User;
import com.concept.conceptbridge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class StudentService {

    private final StudentProfileRepository profileRepo;

    public StudentService(StudentProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public StudentProfile getMyProfile() {
        return profileRepo
                .findByUserUsername(getUsername())
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public StudentProfile updatePersonalInfo(String fullName) {
        StudentProfile profile = getMyProfile();
        profile.updatePersonalInfo(fullName);
        return profile;
    }



    private String getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
