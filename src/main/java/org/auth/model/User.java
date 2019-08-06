package org.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User extends BaseEntity{

    @Column(name = "username", length = 50)
    @Size(max = 50)
    private String username;


    @Column(name = "first_name", length = 50)
    @Size(max = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    @Size(max = 50)
    private String lastName;

    @Column(name = "email", length = 255)
    @Size(max = 255)
    private String email;

    @Column(name = "password", length = 255)
    @Size(max = 255)
    private String password;

    @JsonIgnore
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
    joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

}
