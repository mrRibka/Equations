package com.example.equations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RootService {
    private final RootRepository rootRepository;

    @Autowired
    public RootService(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
    }

    public void save(Root root) {
        rootRepository.save(root);
    }
}
