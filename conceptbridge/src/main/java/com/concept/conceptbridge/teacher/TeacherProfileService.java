package com.concept.conceptbridge.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class TeacherProfileService {

    @Autowired
    private final TeacherProfileRepository profileRepo;

    public TeacherProfileService(TeacherProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    public TeacherProfile getMyProfile() {
        return profileRepo
                .findByUserUsername(getUsername())
                .orElseThrow(() -> new RuntimeException("Teacher profile not found"));
    }

    public TeacherProfile updatePersonalInfo(String fullName) {
        TeacherProfile profile = getMyProfile();
        profile.updatePersonalInfo(fullName);
        return profile;
    }

    private String getUsername() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    public Long getTeacherProfileIdByUsername(String username) {
        return profileRepo.findTeacherIdByUsername(username)
                .orElse(null);
    }

    public Long getUserIdByUsername(String username) {
        return profileRepo.findUserIdByUsername(username)
                .orElse(null);
    }

    public TeacherProfile getTeacherProfileByUsername(String username) {
        return profileRepo.findByUserUsername(username)
                .orElse(null);
    }
}