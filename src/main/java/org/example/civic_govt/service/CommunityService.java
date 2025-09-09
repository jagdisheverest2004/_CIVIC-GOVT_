package org.example.civic_govt.service;

import org.example.civic_govt.model.Community;
import org.example.civic_govt.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {

    @Autowired
    private CommunityRepository communityRepository;

    public Community createCommunity(Community community) {
        return communityRepository.save(community);
    }

    public List<Community> findAllCommunities() {
        return communityRepository.findAll();
    }

    public Optional<Community> findById(Long id) {
        return communityRepository.findById(id);
    }


    public Optional<Community> findByName(String name) {
        return communityRepository.findByName(name);
    }
}