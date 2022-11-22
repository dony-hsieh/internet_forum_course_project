package com.fcu.repository;

import com.fcu.model.PostToTag;
import com.fcu.model.PostToTagId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostToTagRepository extends CrudRepository<PostToTag, PostToTagId>  {

}
