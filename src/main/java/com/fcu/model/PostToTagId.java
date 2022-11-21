package com.fcu.model;

import org.springframework.lang.NonNull;

import java.io.Serializable;

public class PostToTagId implements Serializable {
    private Tag tag;
    private Post post;

    public PostToTagId() {

    }

    public PostToTagId(Tag tag, Post post) {
        this.tag = tag;
        this.post = post;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PostToTagId)) {
            return false;
        }
        PostToTagId another_id = (PostToTagId) obj;
        boolean cond1 = this.tag.getTag_name().equals(another_id.tag.getTag_name());
        boolean cond2 = this.post.getPost_id() == another_id.post.getPost_id();
        return cond1 && cond2;
    }

    @Override
    public int hashCode() {
        int code = 47;
        code = 23 * code + (this.post != null ? this.post.getPost_id() : 0);
        code = 23 * code + (this.tag != null ? this.tag.getTag_name().hashCode() : 0);
        return code;
    }
}
