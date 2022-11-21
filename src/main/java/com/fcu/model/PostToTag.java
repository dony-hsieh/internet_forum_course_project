package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
@Table(name = "PostToTag", schema = "forum")
@IdClass(PostToTagId.class)
public class PostToTag {
    // foreign key which reference Tag.tag_name
    // here is the main controlling side
    @Id
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_name")  // the column name in database
    private Tag tag;  // one PostToTag can only map to one tag

    // foreign key which reference Post.post_id
    // here is the main controlling side
    @Id
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")  // the column name in database
    private Post post;  // one PostToTag can only map to one post

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
