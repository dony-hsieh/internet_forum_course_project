package com.fcu.service;

import com.fcu.repository.UserRepository;
import com.fcu.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements BasicCrudService<User, String> {
    @Autowired
    private UserRepository userRepo;

    public User findOneById(String username) {
        Optional<User> foundUser = this.userRepo.findById(username);
        if (foundUser.isEmpty()) {
            return null;
        }
        return foundUser.get();
    }

    public boolean existsById(String username) {
        return this.userRepo.existsById(username);
    }

    public boolean insertOne(User user) {
        if (user == null || this.userRepo.existsById(user.getUsername())) {
            return false;
        }
        this.userRepo.save(user);
        return true;
    }

    public boolean updateOne(User user) {
        if (user == null || !this.userRepo.existsById(user.getUsername())) {
            return false;
        }
        this.userRepo.save(user);
        return true;
    }

    public boolean deleteOne(User user) {
        if (user == null || !this.userRepo.existsById(user.getUsername())) {
            return false;
        }
        this.userRepo.delete(user);
        return true;
    }
}
