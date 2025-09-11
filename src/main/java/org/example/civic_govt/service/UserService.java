package org.example.civic_govt.service;

import org.example.civic_govt.model.Comment;
import org.example.civic_govt.model.Issue;
import org.example.civic_govt.model.User;
import org.example.civic_govt.payload.users.FetchCitizenDTO;
import org.example.civic_govt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public FetchCitizenDTO createCitizenDTO(User loggedInUser) {
        FetchCitizenDTO dto = new FetchCitizenDTO();
        List<String> reportedIssueTitles = loggedInUser.getReportedIssues().stream()
                .map(Issue::getTitle)
                .toList();
        dto.setId(loggedInUser.getId());
        dto.setUsername(loggedInUser.getUsername());
        dto.setEmail(loggedInUser.getEmail());
        dto.setRole(loggedInUser.getRole().name());
        dto.setReportedIssues(reportedIssueTitles);
        dto.setUpvotedCounts((long) loggedInUser.getVotes().size());
        dto.setCommentedCounts((long) loggedInUser.getComments().size());
        return dto;
    }
}