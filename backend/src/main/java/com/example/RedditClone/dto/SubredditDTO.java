package com.example.RedditClone.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubredditDTO {
    private  String name;
    private Integer postCount;
}
