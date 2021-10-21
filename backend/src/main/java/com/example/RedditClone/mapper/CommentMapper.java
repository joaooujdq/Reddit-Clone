package com.example.RedditClone.mapper;

import com.example.RedditClone.dto.CommentsDTO;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mappings({
    @Mapping(target = "id", ignore = true),
    @Mapping(target = "text", source = "commentsDTO.text"),
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())"),
    @Mapping(target = "post", source = "post"),
    @Mapping(target = "user", source = "user")
    })
    Comment map(CommentsDTO commentsDTO, Post post, User user);

    @Mappings({
            @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())"),
            @Mapping(target = "userName", expression = "java(comment.getUser().getUsername())")
    })
    CommentsDTO mapToDTO(Comment comment);


}


