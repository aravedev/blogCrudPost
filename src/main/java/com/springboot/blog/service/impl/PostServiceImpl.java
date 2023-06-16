package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.PostDto;
import com.springboot.blog.payload.PostResponse;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service // with this we can autowired this class in other classes.
public class PostServiceImpl implements PostService {

    private PostRepository postRepository; // objecto connection to DB

    private ModelMapper mapper;

    // constructor
     public PostServiceImpl(PostRepository postRepository, ModelMapper mapper) {
         this.mapper = mapper;
         this.postRepository = postRepository;
    }

    // Methods

    // converting Entity-(Post) into Dto -(PostDto)
    private PostDto mapToDto(Post post){

         PostDto postDto = mapper.map(post, PostDto.class);

         /*
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setDescription(post.getDescription());
        postDto.setContent(post.getContent());
        */
        return postDto;
    }

    // Converting Dto - (PostDto) into Entity-(Post)
    private Post mapToEntity(PostDto postDto){
        Post post = mapper.map(postDto, Post.class);

         /*
         Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent((postDto.getContent()));
        */
        return post;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
       // Convert Dto to Entity - from JSON (client) to POJO
        Post post = mapToEntity(postDto);

        // saving incoming data in DB and retrieving info
        Post newPost =  postRepository.save(post);

        // convert entity to Dto - from POJO to JSON
        PostDto postResponse = mapToDto(newPost);

        return postResponse;

    }

    // get all post
    @Override
    public PostResponse getAllPosts(int pageNo, int pageSize, String sortBy, String sortDir) {

         // checking if we want to Sort in asc or desc. Note: Sort is a class used on Spring Boot
         Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

         // create pageable intance - choose spring.boot data domain
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

         // get List of Entities - (Post) - findAll(pageable)
         Page<Post> posts = postRepository.findAll(pageable);

         // get content from page object
        List<Post> listOfPosts = posts.getContent();

         // convert Entities to Dtos
         List<PostDto> content= listOfPosts.stream().map(post-> mapToDto(post)).collect(Collectors.toList());

         // creating PostReponse
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNo(posts.getNumber()); // posts is the page object
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElements(posts.getTotalElements());
        postResponse.setTotalPages(posts.getTotalPages());
        postResponse.setLast(posts.isLast());

        return postResponse;

    }

    @Override
    public PostDto getPostById(Long id) {
        // getting PostById - Entity
        Post post = postRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Post","id",id));

        // converting from Enty to to Dto
        PostDto postResponse = mapToDto(post);
        return postResponse;
    }

    //
    @Override
    public PostDto updatePost(PostDto postDto, Long id) {
        // Getting entity from DB
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id));

        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());

        // save changes into DB
        Post updatedPost = postRepository.save(post);

        // return result
        return mapToDto(updatedPost);

    }

    @Override
    public void deletePostById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","Id",id));
        postRepository.delete(post);
    }


}
