package com.example.RedditClone.service;

import com.example.RedditClone.dto.CommentsDTO;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.PostRepository;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public List<CommentsDTO> getAll(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()-> new PostNotFoundException(POST_NOT_FOUND_FOR_ID + postId));
        return commentRepository.findByPost(post)
                .stream()
                .map(this::mapToDTO)
                .collect(toList());
    }

    private CommentsDTO mapToDTO(Comment comment){
        return CommentsDTO.builder().id(comment.getId())
                .text(comment.getText())
                .duration(calcDuration(comment.getCreatedDate()))
                .build();
    }
}
