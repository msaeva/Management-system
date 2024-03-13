package com.example.management_system.service;

import com.example.management_system.controller.UserController;
import com.example.management_system.domain.dto.RegisterUserValidation;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRepository;
import com.example.management_system.service.mapper.UserMapper;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    public void create(RegisterUserValidation validation) {
        User user = userMapper.toUser(validation);
        userRepository.save(user);
    }

    public Set<User> findByIds(List<Long> userIds) {
        return new HashSet<>(userRepository.findByIds(userIds));
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }
}
