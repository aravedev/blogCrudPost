package com.springboot.blog.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="comments", schema = "myblog")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     private String name;
     private String email;
     private String body;

     @ManyToOne(fetch = FetchType.LAZY) // Many comments belongs to One Post
     @JoinColumn(name="post_id", nullable = false) // specify the foreign key, it cant be null
     private Post post; // post variable created to One to Many
}
