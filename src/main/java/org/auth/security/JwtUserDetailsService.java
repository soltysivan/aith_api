package org.auth.security;

import lombok.extern.slf4j.Slf4j;
import org.auth.exceptions.NotFoundException;
import org.auth.model.User;
import org.auth.repository.UserRepository;
import org.auth.security.jwt.JwtUser;
import org.auth.security.jwt.JwtUserFactory;
import org.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired private UserService userService;

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);

        JwtUser jwtUser = JwtUserFactory.of(user);
        log.info("IN loadUserByUsername user with user name: {} successfully loaded", username);
        return jwtUser;
    }
}
