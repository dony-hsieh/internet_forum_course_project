package com.fcu.service;

import com.fcu.model.Tag;
import com.fcu.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TagService implements BasicCrudService<Tag, String> {
    @Autowired
    private TagRepository tagRepo;

    public Tag findOneById(String tagName) {
        Optional<Tag> foundTag = this.tagRepo.findById(tagName);
        if (foundTag.isEmpty()) {
            return null;
        }
        return foundTag.get();
    }

    public boolean insertOne(Tag tag) {
        if (tag == null || this.tagRepo.existsById(tag.getTagName())) {
            return false;
        }
        this.tagRepo.save(tag);
        return true;
    }

    public boolean updateOne(Tag tag) {
        if (tag == null || !this.tagRepo.existsById(tag.getTagName())) {
            return false;
        }
        this.tagRepo.save(tag);
        return true;
    }

    public boolean deleteOne(Tag tag) {
        if (tag == null || !this.tagRepo.existsById(tag.getTagName())) {
            return false;
        }
        this.tagRepo.delete(tag);
        return true;
    }
}
