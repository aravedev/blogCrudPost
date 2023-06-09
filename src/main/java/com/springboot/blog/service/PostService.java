package com.springboot.blog.service;

import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;

import java.util.List;

public interface PostService {
    // this interface contains all the methods used for the API and will hide all the methods used.
    // this is done for security reasons.

    // create post method - using Dto.
    PostDto createPost(PostDto postDto);

    // get all post
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir);

    // get Post by Id
    public PostDto getPostById(Long id);

    // Update Post by Id
    PostDto updatePost(PostDto postDto, Long id);

    // delete Post by id
    void deletePostById(Long id);
}
