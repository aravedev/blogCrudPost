package com.springboot.blog.entity;

import jakarta.persistence.*;
import lombok.*;

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
}
