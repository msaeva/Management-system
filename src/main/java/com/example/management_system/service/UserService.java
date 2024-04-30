package com.example.management_system.service;

import com.example.management_system.controller.errors.InvalidUserException;
import com.example.management_system.controller.errors.UserNotFoundException;
import com.example.management_system.domain.dto.Pagination;
import com.example.management_system.domain.dto.project.DetailedProjectDTO;
import com.example.management_system.domain.dto.user.*;
import com.example.management_system.domain.entity.Task;
import com.example.management_system.domain.entity.Team;
import com.example.management_system.domain.entity.User;
import com.example.management_system.domain.entity.UserRole;
import com.example.management_system.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.*;
import java.util.stream.Collectors;

@Stateless
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private UserRoleService userRoleService;

    @Inject
    private ProjectService projectService;

    @Inject
    private AuthService authService;

    @Inject
    private TaskService taskService;

    @Transactional
    public SimpleUserDTO create(RegisterUserValidation validation) {

        Optional<User> byUsername = userRepository.findByUsername(validation.getUsername());
        Optional<User> byEmail = userRepository.findByEmail(validation.getEmail());

        if (byUsername.isPresent() || byEmail.isPresent()) {
            throw new InvalidUserException("User already exists!");
        }

        String encodedPassword = encodePassword(validation.getPassword());
        UserRole role;
        if (validation.getRole() == null || validation.getRole().isEmpty()) {
            role = userRoleService.findByName("USER");
        } else {
            role = userRoleService.findByName(validation.getRole());
        }

        User user = new User(validation.getUsername(),
                validation.getFirstName(),
                validation.getLastName(),
                validation.getEmail(),
                encodedPassword,
                role,
                false);

        User saved = userRepository.save(user);
        return mapToSimpleUserDTO(saved);
    }

    public boolean deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            for (Team team : user.getTeams()) {
                team.getUsers().removeIf(u -> Objects.equals(u.getId(), user.getId()));
            }

            if (!user.getTasks().isEmpty()) {
                List<Long> taskIds = user
                        .getTasks()
                        .stream()
                        .map(Task::getId)
                        .collect(Collectors.toList());

                this.taskService.removeAssignedUserFromTasks(taskIds);
            }

            user.setDeleted(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public DetailedUserDTO update(UpdateUserValidation validation) {
        User user = findById(validation.getId());
        UserRole role = userRoleService.findByName(validation.getRole());

        user.setFirstName(validation.getFirstName());
        user.setLastName(validation.getLastName());
        user.setRole(role);
        User updated = userRepository.save(user);
        return maptoDetailedUserDTO(updated);
    }

    public List<PrivateSimpleUserDTO> getByRoles(String roles) {
        List<UserRole> userRoles = Arrays.stream(roles.split(","))
                .map(role -> userRoleService.findByName(role))
                .collect(Collectors.toList());

        return userRepository.findAllByRoleIds(userRoles.stream().map(UserRole::getId).collect(Collectors.toList()))
                .stream()
                .map(this::mapToPrivateSimpleUserDTO)
                .collect(Collectors.toList());
    }

    public Set<User> findAllByIds(List<Long> userIds) {
        return new HashSet<>(userRepository.findAllByIds(userIds));
    }

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id : " + id + " not found!"));
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

    public SimpleUserDTO mapToSimpleUserDTO(User user) {
        return new SimpleUserDTO(user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().getRole().name());
    }

    public PrivateSimpleUserDTO mapToPrivateSimpleUserDTO(User user) {
        return new PrivateSimpleUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName() + " " + user.getLastName(),
                user.getRole().getRole().name());
    }

    public Pagination<DetailedUserDTO> getAll(int page, int size, String sort, String order) {
        List<User> users = new ArrayList<>(userRepository
                .findAll(page, size, sort, order));

        List<DetailedUserDTO> result = new ArrayList<>();
        for (User user : users) {
            List<DetailedProjectDTO> projects = projectService.getProjectsByUserId(user.getId());
            result.add(maptoDetailedUserDTO(user, projects));

        }

        long totalRecords = userRepository.getTotalCount();
        return new Pagination<>(result, totalRecords);
    }

    public DetailedUserDTO maptoDetailedUserDTO(User user, List<DetailedProjectDTO> projects) {
        return new DetailedUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRole().name(),
                projects);
    }

    public DetailedUserDTO maptoDetailedUserDTO(User user) {
        return new DetailedUserDTO(user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole().getRole().name());
    }

    public DetailedUserDTO getUserProfile() {
        User authUser = authService.getAuthenticatedUser();

        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }

        User user = findById(authUser.getId());
        return maptoDetailedUserDTO(user, null);
    }

    public boolean changePassword(ChangePasswordValidation validation) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }

        if (!checkPassword(validation.getOldPassword(), authUser.getPassword())) {
            throw new InvalidUserException("Invalid password");
        }

        String encodedPassword = encodePassword(validation.getNewPassword());
        authUser.setPassword(encodedPassword);
        userRepository.save(authUser);

        return true;
    }

    public DetailedUserDTO updateUser(UpdateUserValidation validation) {
        User authUser = authService.getAuthenticatedUser();
        if (authUser == null) {
            throw new InvalidUserException("User is not authenticated");
        }

        authUser.setFirstName(validation.getFirstName());
        authUser.setLastName(validation.getLastName());
        User saved = userRepository.save(authUser);
        return maptoDetailedUserDTO(saved, null);
    }

    public List<User> getAllUsersByProject(long projectId, int page, int size, String search) {
      return   userRepository.getAllUsersByProject(projectId, page, size, search);
    }

    public long getCountAllUsersByProject(Long id, String search) {
       return userRepository.getCountAllUsersByProject(id, search);
    }
}
