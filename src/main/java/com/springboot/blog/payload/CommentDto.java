package com.springboot.blog.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotEmpty
    @Size(min = 2, message = "Name should not be Empty and min 2 characters")
    private String name;

    @NotEmpty(message = "Email can not be null or empty")
    @Email
    private String email;

    @NotEmpty
    @Size(min=2, message = "Body must be min 2 characters")
    private String body;
}
