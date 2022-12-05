package com.fcu.repository;

import com.fcu.model.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByTitleContaining(String title, Pageable pageable);
}
