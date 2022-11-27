package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "User", schema = "forum")
public class User {
    @Id
    @Column(name = "username", length = 20, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "enable", nullable = false, insertable = false)
    private boolean enable;

    @Column(name = "privilege", nullable = false, insertable = false)
    private boolean privilege;

    // referenced by Post.username
    // here is the side which is controlled
    @JsonManagedReference
    @OneToMany(
            // the attribute name of the outer entity which references this entity
            mappedBy = "user",
            // set cascade type
            cascade = CascadeType.ALL,
            // it is like "on delete cascade" but in the application layer instead of DB layer
            orphanRemoval = true,
            // delay fetch
            fetch = FetchType.LAZY
    )
    private List<Post> posts;  // one user can map to many posts

    // referenced by Comment.username
    // here is the side which is controlled
    @JsonManagedReference
    @OneToMany(
            // the attribute name of the outer entity which references this entity
            mappedBy = "user",
            // set cascade type
            cascade = CascadeType.ALL,
            // it is like "on delete cascade" but in the application layer instead of DB layer
            orphanRemoval = true,
            // delay fetch
            fetch = FetchType.LAZY
    )
    private List<Comment> comments;  // one user can map to many comments

    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private EmailVerification emailVerification;

    public User() {

    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isPrivilege() {
        return privilege;
    }

    public void setPrivilege(boolean privilege) {
        this.privilege = privilege;
    }
}
