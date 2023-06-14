package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name="post", schema = "myblog",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="description", nullable = false)
    private String description;

    @Column(name="content", nullable = false)
    private String content;

    // Making the relationship to comments
    // One post belongs to Multiple comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true) // this post is the post variable created on Comments
    private Set<Comment> comments = new HashSet<>();
}
