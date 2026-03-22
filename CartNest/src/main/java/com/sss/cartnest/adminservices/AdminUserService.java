package com.sss.cartnest.adminservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sss.cartnest.entities.User;
import com.sss.cartnest.repositories.UserRepository;

@Service
public class AdminUserService {

    @Autowired
    private UserRepository user_repo;

    public void deleteUser(String username) {
        User user = user_repo.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        user_repo.deleteById(user.getUser_id());
    }
}