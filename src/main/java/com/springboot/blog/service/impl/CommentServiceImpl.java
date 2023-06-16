package com.springboot.blog.service.impl;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.Post;
import com.springboot.blog.exception.BlogApiException;
import com.springboot.blog.exception.ResourceNotFoundException;
import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.repository.CommentRepository;
import com.springboot.blog.repository.PostRepository;
import com.springboot.blog.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    // Create the connection with the DB
    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private ModelMapper mapper;

    // Constructor
    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository, ModelMapper mapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.mapper = mapper;
    }

    // methods
    // From Entity to Dto
    private CommentDto mapToDTO(Comment comment){

        CommentDto commentDto = mapper.map(comment, CommentDto.class);

        /*
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setName(comment.getName());
        commentDto.setEmail(comment.getEmail());
        commentDto.setBody(comment.getBody());
        */
        return commentDto;
    }

    // From Dto to Entity
    private Comment mapToEntity(CommentDto commentDto){

        Comment comment = mapper.map(commentDto, Comment.class);

        /*
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        */
        return comment;
    }

    @Override
    public CommentDto createComment(Long postId, CommentDto commentDto) {
        // Convert Dto to Entity
        Comment comment = mapToEntity(commentDto);

        // Retrieve entity(Post) by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        // Set post to comment entity - creating the connection between post and comment
        comment.setPost(post);

        // save comment entity to DB
        Comment newComment = commentRepository.save(comment);

        // converting comment entity to Dto and returning the result
        return mapToDTO(newComment);

    }

    @Override
    public List<CommentDto> getCommentsByPostId(Long postId) {
        // retrieve comments by postId
        List<Comment> comments = commentRepository.findByPostId(postId);

        // Convert comment entity to Dto
        return comments.stream().map(comment->mapToDTO(comment)).collect(Collectors.toList());

    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        // Retrieve entity(Post) by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                 ()->new ResourceNotFoundException("Comment","Id",commentId));

        // if comment contains specific post, doesnt match the post Id, throw exception
        // ex: I call post 2 and comment 1 but, comment 1 is related to post 1, then both id dont match
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }

    // Update comment by id
    @Override
    public CommentDto updateCommentById(Long postId, Long commentId, CommentDto commentRequest) {

        // Retrieve entity(Post) by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment","Id",commentId));

        // check if the comments belongs to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belog to post");

        }

        // update post
        comment.setName(commentRequest.getName());
        comment.setEmail(commentRequest.getEmail());
        comment.setBody(commentRequest.getBody());

        Comment updatedComment = commentRepository.save(comment);

        return mapToDTO(updatedComment);

    }

    @Override
    public void deleteCommentById(Long postId, Long commentId) {
        // Retrieve entity(Post) by id
        Post post = postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post", "id", postId));

        // Retrieve comment by id
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                ()->new ResourceNotFoundException("Comment","Id",commentId));

        // check if the comments belongs to the post
        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogApiException(HttpStatus.BAD_REQUEST, "Comment does not belog to post");

        }

        commentRepository.deleteById(comment.getId());
    }


}
