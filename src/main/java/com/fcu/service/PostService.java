package com.fcu.service;

import com.fcu.model.Post;
import com.fcu.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostService implements BasicCrudService<Post, Integer> {
    @Autowired
    private PostRepository postRepo;

    public Post findOneById(Integer postId) {
        Optional<Post> foundPost = this.postRepo.findById(postId);
        if (foundPost.isEmpty()) {
            return null;
        }
        return foundPost.get();
    }

    public boolean insertOne(Post post) {
        if (post == null || this.postRepo.existsById(post.getPostId())) {
            return false;
        }
        this.postRepo.save(post);
        return true;
    }

    public boolean updateOne(Post post) {
        if (post == null || !this.postRepo.existsById(post.getPostId())) {
            return false;
        }
        this.postRepo.save(post);
        return true;
    }

    public boolean deleteOne(Post post) {
        if (post == null || !this.postRepo.existsById(post.getPostId())) {
            return false;
        }
        this.postRepo.delete(post);
        return true;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepo.findAll(pageable);
    }

    public Page<Post> findByTitleContaining(String title, Pageable pageable) {
        return postRepo.findByTitleContaining(title, pageable);
    }
}
