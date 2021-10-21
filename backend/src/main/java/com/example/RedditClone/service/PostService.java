package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostRequest;
import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.exceptions.PostNotFoundException;
import com.example.RedditClone.exceptions.SubredditNotFoundException;
import com.example.RedditClone.mapper.PostMapper;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.model.User;
import com.example.RedditClone.model.Vote;
import com.example.RedditClone.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.Valid;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.RedditClone.model.VoteType.UPVOTE;
import static com.example.RedditClone.util.Constants.POST_NOT_FOUND_FOR_ID;
import static com.example.RedditClone.util.Constants.SUBREDDIT_NOT_FOUND_WITH_ID;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        return postMapper.mapToDTO(post);
    }

    public void save(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                        .orElseThrow(()-> new SubredditNotFoundException(postRequest.getSubredditName()));
        postRepository.save(postMapper.map(postRequest, subreddit, authService.getCurrentUser()));

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDTO)
                .collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDTO).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDTO)
                .collect(toList());
    }
}
