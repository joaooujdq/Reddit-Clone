package com.example.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PostRequest {

    private Long postId;

    @NotNull(message =  "Subreddit Id é necessário")
    private Long subredditId;

    @NotBlank(message = "O nome do post é necessário")
    private String postName;
    private String url;
    private String description;

    @NotBlank(message="O nome de usuário é necessário")
    private String userName;
}
