package org.auth.service;

import lombok.extern.slf4j.Slf4j;
import org.auth.exceptions.NotFoundException;
import org.auth.model.Role;
import org.auth.model.Status;
import org.auth.model.User;
import org.auth.repository.RoleRepository;
import org.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired private UserRepository userRepository;

    @Autowired private RoleRepository roleRepository;

    @Autowired private BCryptPasswordEncoder passwordEncoder;

    public User saveNewUser(User user){
        Role roleUser = roleRepository.findRoleByName("ROLE_USER");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(roleUser);
        user.setStatus(Status.ACTIVE);
        userRepository.save(user);
        log.info("IN saveNewUser - user: {} successfully registered", user);
        return user;
    }

    public List<User> getAllUsers(){
        List<User> users = userRepository.findAll();
        log.info("IN getAllUsers - {} users found", users.size());
        return users;
    }

    public User getUserById(Long id){
        User user = userRepository.getUserById(id)
                .orElseThrow(NotFoundException::new);
        log.info("IN getUserById - user: {} found by user id: {}", user, id);
        return user;
    }

    public User getUserByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
        log.info("IN getUserByUsername - user: {} found by user name: {}", user, username);
        return user;
    }

    public User updateUser(Long id, User user){
        User userFromDB = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        userFromDB.setFirstName(user.getFirstName());
        userFromDB.setLastName(user.getLastName());
        userFromDB.setEmail(user.getEmail());
        userRepository.save(userFromDB);
        log.info("IN updateUser - user: {} successfully updated", userFromDB);
        return userFromDB;
    }

    public User deleteUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        user.setStatus(Status.DELETED);
        userRepository.save(user);
        log.info("IN deleteUserById - user: {} successfully deleted", user);
        return user;
    }
}
