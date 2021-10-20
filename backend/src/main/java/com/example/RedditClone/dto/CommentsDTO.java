package com.example.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommentsDTO {
    private Long id;
    private String text;
    private  String duration;
}
