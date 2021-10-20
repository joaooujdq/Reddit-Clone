package com.example.RedditClone.dto;

import com.example.RedditClone.model.VoteType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VoteDTO {
    private VoteType voteType;
    private String postName;
    private String userName;
}
