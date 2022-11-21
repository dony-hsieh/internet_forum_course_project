package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;

@Entity
@Table(name = "Post")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private int post_id;

    @Column(name = "title", length = 150, nullable = false)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "voting_up", nullable = false)
    private int voting_up;

    @Column(name = "voting_down", nullable = false)
    private int voting_down;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "post_date", nullable = false)
    private Date post_date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modify_date", nullable = false)
    private Date modify_date;

    // foreign key which reference User.username
    // here is the main controlling side
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")  // the column name in database
    private User user;  // one post can only map to one user

    @Column(name = "parent_post_id")
    private int parent_post_id;

    // referenced by Comment.post_id
    // here is the side which is controlled
    @JsonManagedReference
    @OneToMany(
            // the attribute name of the outer entity which references this entity
            mappedBy = "post",
            // set cascade type
            cascade = CascadeType.ALL,
            // it is like "on delete cascade" but in the application layer instead of DB layer
            orphanRemoval = true,
            // delay fetch
            fetch = FetchType.LAZY
    )
    private ArrayList<Comment> comments;  // one post can map to many comments

    // referenced by PostToTag.post_id
    // here is the side which is controlled
    @JsonManagedReference
    @OneToMany(
            // the attribute name of the outer entity which references this entity
            mappedBy = "post",
            // set cascade type
            cascade = CascadeType.ALL,
            // it is like "on delete cascade" but in the application layer instead of DB layer
            orphanRemoval = true,
            // delay fetch
            fetch = FetchType.LAZY
    )
    private ArrayList<PostToTag> postToTags;

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getVoting_up() {
        return voting_up;
    }

    public void setVoting_up(int voting_up) {
        this.voting_up = voting_up;
    }

    public int getVoting_down() {
        return voting_down;
    }

    public void setVoting_down(int voting_down) {
        this.voting_down = voting_down;
    }

    public Date getPost_date() {
        return post_date;
    }

    public void setPost_date(Date post_date) {
        this.post_date = post_date;
    }

    public Date getModify_date() {
        return modify_date;
    }

    public void setModify_date(Date modify_date) {
        this.modify_date = modify_date;
    }

    public int getParent_post_id() {
        return parent_post_id;
    }

    public void setParent_post_id(int parent_post_id) {
        this.parent_post_id = parent_post_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
