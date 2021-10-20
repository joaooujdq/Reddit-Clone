package com.example.RedditClone.controller;

import com.example.RedditClone.dto.PostResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(SUBREDDIT_API_MAPPING)
@AllArgsConstructor
public class SubredditController {
    private final SubredditService subredditService;

    @GetMapping(QUERY_ALL)
    public List<SubredditDTO> getAllSubreddits(){
        return subredditService.getAll();
    }

    @GetMapping(ID_POSTS_ALL)
    public List<PostResponse> getAllPostsinSubreddit(@PathVariable Long id){
        return  subredditService.getAllPosts(id);
    }
}
