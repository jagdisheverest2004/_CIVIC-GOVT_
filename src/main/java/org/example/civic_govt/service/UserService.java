package org.example.civic_govt.service;

import org.example.civic_govt.model.User;
import org.example.civic_govt.repository.IssueRepository;
import org.example.civic_govt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IssueRepository issueRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

}