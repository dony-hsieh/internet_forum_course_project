package com.fcu.service;

import com.fcu.model.Comment;
import com.fcu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService implements BasicCrudService<Comment, Integer> {
    @Autowired
    private CommentRepository commentRepo;

    public Comment findOneById(Integer commentId) {
        Optional<Comment> foundComment = this.commentRepo.findById(commentId);
        if (foundComment.isEmpty()) {
            return null;
        }
        return foundComment.get();
    }

    public boolean insertOne(Comment comment) {
        if (comment == null || this.commentRepo.existsById(comment.getCommentId())) {
            return false;
        }
        this.commentRepo.save(comment);
        return true;
    }

    public boolean updateOne(Comment comment) {
        if (comment == null || !this.commentRepo.existsById(comment.getCommentId())) {
            return false;
        }
        this.commentRepo.save(comment);
        return true;
    }

    public boolean deleteOne(Comment comment) {
        if (comment == null || !this.commentRepo.existsById(comment.getCommentId())) {
            return false;
        }
        this.commentRepo.delete(comment);
        return true;
    }
}
