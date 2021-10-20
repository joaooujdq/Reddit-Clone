package com.example.RedditClone.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {
    private final CommentService commentService;

    @GetMapping("/query/{postId}")
    public List<CommentsDTO> getAllCommentsForPost(@PathVariable Long postId){
        return commentService.getAll(postId);
    }
}
