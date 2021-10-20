package com.example.RedditClone.dto;

import com.example.RedditClone.model.Vote;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
public class PostResponse {
    private String postName;
    private String url;
    private String description;
    private List<Vote> votes;
    private String userName;
    private boolean upVote;
    private boolean downVote;
    private String subredditName;
    private Integer commentNum;
}
