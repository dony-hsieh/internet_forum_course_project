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
        boolean cond1 = this.tag.getTagName().equals(another_id.tag.getTagName());
        boolean cond2 = this.post.getPostId() == another_id.post.getPostId();
        return cond1 && cond2;
    }

    @Override
    public int hashCode() {
        int code = 47;
        code = 23 * code + (this.post != null ? this.post.getPostId() : 0);
        code = 23 * code + (this.tag != null ? this.tag.getTagName().hashCode() : 0);
        return code;
    }
}
