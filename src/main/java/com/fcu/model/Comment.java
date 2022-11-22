package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Comment", schema = "forum")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private int comment_id;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modify_date", nullable = false)
    private Date modify_date;

    // foreign key which reference Post.post_id
    // here is the main controlling side
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")  // the column name in database
    private Post post;  // one comment can only map to one post

    // foreign key which reference User.username
    // here is the main controlling side
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")  // the column name in database
    private User user;  // one comment can only map to one user

    public int getCommentId() {
        return comment_id;
    }

    public void setCommentId(int comment_id) {
        this.comment_id = comment_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getModifyDate() {
        return modify_date;
    }

    public void setModifyDate(Date modify_date) {
        this.modify_date = modify_date;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
