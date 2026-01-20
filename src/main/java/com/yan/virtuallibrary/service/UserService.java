package com.yan.virtuallibrary.service;

import com.yan.virtuallibrary.domain.UserEntity;
import com.yan.virtuallibrary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserEntity execute(UserEntity userEntity) {
        return userRepository.save(userEntity);

    }

}
