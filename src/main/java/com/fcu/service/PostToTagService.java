package com.fcu.service;

import com.fcu.model.PostToTag;
import com.fcu.model.PostToTagId;
import com.fcu.repository.PostToTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostToTagService implements BasicCrudService<PostToTag, PostToTagId> {
    @Autowired
    private PostToTagRepository postToTagRepo;

    public PostToTag findOneById(PostToTagId postToTagId) {
        Optional<PostToTag> foundPostToTag = this.postToTagRepo.findById(postToTagId);
        if (foundPostToTag.isEmpty()) {
            return null;
        }
        return foundPostToTag.get();
    }

    public boolean insertOne(PostToTag postToTag) {
        if (postToTag == null || this.postToTagRepo.existsById(postToTag.getId())) {
            return false;
        }
        this.postToTagRepo.save(postToTag);
        return true;
    }

    public boolean updateOne(PostToTag postToTag) {
        if (postToTag == null || !this.postToTagRepo.existsById(postToTag.getId())) {
            return false;
        }
        this.postToTagRepo.save(postToTag);
        return true;
    }

    public boolean deleteOne(PostToTag postToTag) {
        if (postToTag == null || !this.postToTagRepo.existsById(postToTag.getId())) {
            return false;
        }
        this.postToTagRepo.delete(postToTag);
        return true;
    }
}
