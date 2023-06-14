package com.springboot.blog.controller;

import com.springboot.blog.payload.CommentDto;
import com.springboot.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    // constructor

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // Methods
    // Note: from JSON to POJO use RequestBody
    @PostMapping("/post/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(value= "postId")Long postId ,
                                                    @RequestBody CommentDto commentDto ){

        // ResponseEntity<>(commentService Object calls method createComment )
        return new ResponseEntity<>(commentService.createComment(postId, commentDto), HttpStatus.CREATED);
    }

    // Get comments by postId
    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentsByPostId(@PathVariable(value="postId") Long postId){
        return commentService.getCommentsByPostId(postId);
    }

    // Get comment by id

    @GetMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable(value="postId") Long postId,
                                                     @PathVariable(value="id") Long commentId){

        CommentDto commentDto = commentService.getCommentById(postId,commentId);

        return new ResponseEntity<>(commentDto, HttpStatus.OK);
    }

    // Update comment
    @PutMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable(value = "postId") Long postId,
                                                    @PathVariable(value = "commentId")Long commentId,
                                                    @RequestBody CommentDto commentDto){

        CommentDto updatedComment = commentService.updateCommentById(postId, commentId, commentDto);
        return new ResponseEntity<>(updatedComment ,HttpStatus.OK);

    }

    @DeleteMapping("/posts/{postId}/comments/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable(value="postId") Long postId,
                                                @PathVariable(value="id") Long commentId){

        commentService.deleteCommentById(postId, commentId);

        return new ResponseEntity<>("Comment deleted successfully", HttpStatus.OK);


    }
}
