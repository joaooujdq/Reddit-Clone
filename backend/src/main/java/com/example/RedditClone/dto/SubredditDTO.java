package com.example.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubredditDTO {
    private Long id;
    private String description;
    private  String name;
    private Integer numberOfPosts;
}
