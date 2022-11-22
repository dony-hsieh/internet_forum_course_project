package com.fcu.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tag", schema = "forum")
public class Tag {

    @Id
    @Column(name = "tag_name", length = 20, nullable = false)
    private String tag_name;

    // referenced by PostToTag.tag_name
    // here is the side which is controlled
    @JsonManagedReference
    @OneToMany(
            // the attribute name of the outer entity which references this entity
            mappedBy = "tag",
            // set cascade type
            cascade = CascadeType.ALL,
            // it is like "on delete cascade" but in the application layer instead of DB layer
            orphanRemoval = true,
            // delay fetch
            fetch = FetchType.LAZY
    )
    private List<PostToTag> postToTags;  // one tag can map to many PostToTags

    public String getTagName() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
