package org.auth.controller;

import org.auth.dto.AuthenticationRequestDto;
import org.auth.exceptions.NotFoundException;
import org.auth.model.Role;
import org.auth.model.Status;
import org.auth.model.User;
import org.auth.repository.RoleRepository;
import org.auth.repository.UserRepository;
import org.auth.security.jwt.JwtTokenProvider;
import org.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/auth/")
public class AuthenticationController {

    @Autowired private AuthenticationManager authenticationManager;

    @Autowired private JwtTokenProvider jwtTokenProvider;

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @Autowired private RoleRepository roleRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthenticationRequestDto requestDto, HttpServletResponse httpServletResponse){
        try {
            String username = requestDto.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, requestDto.getPassword()));
            User user = userRepository.findByUsername(username)
                    .orElseThrow(NotFoundException::new);
            String token = jwtTokenProvider.createToken(username, user.getRoles());
            Map<Object, Object> response = new HashMap<>();
            response.put("username", username);
            response.put("token", token);
            httpServletResponse.setHeader("token", token);
            return ResponseEntity.ok(username);
        }catch (Exception e){
            throw new BadCredentialsException("Invalid user name or password");
        }
    }

    @PostMapping("registration")
    public ResponseEntity createNewUser(@RequestBody User user){
        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findById(1L)
                .orElseThrow(NotFoundException::new);
        roles.add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreated(Date.valueOf(LocalDate.now()));
        user.setStatus(Status.ACTIVE);
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
