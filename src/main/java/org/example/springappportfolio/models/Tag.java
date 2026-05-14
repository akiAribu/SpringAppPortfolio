package org.example.springappportfolio.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id", nullable = false)
    private Integer id;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag_type", nullable = false)
    private TagType tagType;

    @OneToMany(mappedBy = "tag")
    private Set<TagInProject> projects = new LinkedHashSet<>();

}
