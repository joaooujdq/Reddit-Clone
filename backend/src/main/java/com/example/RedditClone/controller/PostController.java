package com.example.RedditClone.controller;

import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.example.RedditClone.util.ApiPaths.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(POST_API_MAPPINGS)
@AllArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping(QUERY_ID)
    public PostResponse getPost(@PathVariable Long id){
        return postService.getPost(id);
    }

    @GetMapping(QUERY_ALL)
    public List<PostResponse> getPost(){
        return postService.getAllPosts();
    }

    @PostMapping(CREATE)
    public ResponseEntity createPost(@Valid @RequestBody PostRequest postRequest){
        postService.save(postRequest);
        return new ResponseEntity(OK);
    }

    @PostMapping(VOTE_ID)
    public  ResponseEntity vote(@Valid @RequestBody VoteDTO voteDTO, @PathVariable Long id){
        postService.vote(voteDTO, id);
        return  new ResponseEntity(OK);
    }
}
