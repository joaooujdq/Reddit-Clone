package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.model.Vote;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.SubredditRepository;
import com.example.RedditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.validation.Valid;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.RedditClone.model.VoteType.UPVOTE;
import static com.example.RedditClone.util.Constants.POST_NOT_FOUND_FOR_ID;
import static com.example.RedditClone.util.Constants.SUBREDDIT_NOT_FOUND_WITH_ID;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final VoteRepository voteRepository;
    private final SubredditRepository subredditRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(POST_NOT_FOUND_FOR_ID + id));
        PostResponse postResponse = mapToDTO(post);
        PostResponse.setCommentNum(CommentRepository.findByPost(post).size());
        return postResponse;
    }

    static PostResponse mapToDTO(Post post){
        return PostResponse.builder()
                .postName(post.getPostName())
                .description(post.getDescription())
                .url(post.getUrl())
                .userName(post.getUser().getUsername())
                .subredditName(post.getSubreddit().getName())
                .build();
    }

    @Transactional
    public void save(@Valid PostRequest postRequest){
        postRepository.save(mapToPost(postRequest));
    }

    private Post mapToPost(PostRequest postRequest){
        Subreddit subreddit = subredditRepository.findById(postRequest.getSubredditId())
                .orElseThrow(() -> new SubredditNotFoundException(SUBREDDIT_NOT_FOUND_WITHH_ID + postRequest.getSubredditId()));
        return Post.builder()
                .postName(postRequest.getPostName())
                .description(postRequest.getDescription())
                .url(postRequest.getUrl())
                .createdDate(Instant.now())
                .voteCount(0)
                .subreddit(subreddit)
                .build();
    }
    public List<PostResponse> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(PostService::mapToDTO)
                .collect(Collectors.toList());
    }

    public synchronized void vote(VoteDTO voteDTO, Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotfoundException("Post não encontrado com ID - " + id));
        int count = 0;
        if(UPVOTE.equals(voteDTO.getVoteType())){
            count = post.getVoteCount() + 1;
        }else{
            count = post.getVoteCount() - 1;
        }
        voteRepository.save(mapToDTO(voteDTO, post));
        post.setVoteCount(count);
        postRepository.save(post);
    }
    private Vote mapToVote(VoteDTO voteDTO, Post, post){
        return Vote.builder()
                .voteType(voteDTO.getVoteType())
                .post(post)
                .build();
    }
}
