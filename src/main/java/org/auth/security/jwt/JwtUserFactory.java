package org.auth.security.jwt;

import org.auth.model.Role;
import org.auth.model.Status;
import org.auth.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    public JwtUserFactory(){
    }

    public static JwtUser of(User user){
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getEmail(),
                user.getStatus().equals(Status.ACTIVE),
                user.getCreated(),
                mapToGrantedAuthority(new ArrayList<>(user.getRoles()))

        );
    }
            //проходимся по списку ролей і перетворюєм в GrandAuthorities
    private static List<GrantedAuthority> mapToGrantedAuthority(List<Role> roles){
        return roles.stream()
                .map(role ->
                    new SimpleGrantedAuthority(role.getName())
                ).collect(Collectors.toList());
    }
}
