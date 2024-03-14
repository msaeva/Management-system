package com.example.management_system.service;

import com.example.management_system.controller.errors.UserNotFoundException;
import com.example.management_system.domain.dto.RegisterUserValidation;
import com.example.management_system.domain.entity.User;
import com.example.management_system.repository.UserRepository;
import com.example.management_system.service.mapper.UserMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

@Stateless
public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private UserMapper userMapper;

    @Inject
    private UserRoleService userRoleService;


    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());

    @Transactional
    public User create(RegisterUserValidation validation) {


        // TODO check by username and email
        String encodedPassword = encodePassword(validation.getPassword());

        User user = userMapper.toUser(validation);
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }


    public Set<User> findAllByIds(List<Long> userIds) {
        return new HashSet<>(userRepository.findAllByIds(userIds));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found!"));
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    private String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }


    public boolean checkPassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }

}
