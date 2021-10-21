package com.example.RedditClone.dto;

import com.example.RedditClone.model.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String postName;
    private String url;
    private String description;
    private String userName;
    private String subredditName;
    private boolean upVote;
    private boolean downVote;
    private String duration;
    private Integer voteCount;
    private Integer commentCount;
}
