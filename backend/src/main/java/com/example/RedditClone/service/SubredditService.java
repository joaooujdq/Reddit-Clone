package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.dto.SubredditDTO;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.RedditClone.util.Constants.SUBREDDIT_NOT_FOUND_WITH_ID;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final PostRepository postRepository;

    public List<SubredditDTO> getAll(){
        return subredditRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(toList());
    }

    private  SubredditDTO mapToDTO(Subreddit subreddit){
        return  SubredditDTO.builder().name(subreddit.getName())
                .postCount(subreddit.getPosts().size())
                .build();
    }

    public List<PostResponse> getAllPosts(Long id){
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SubredditNotFoundException(SUBREDDIT_NOT_FOUND_WITH_ID + id));
        return postRepository.findAllBySubreddit(subreddit)
                .stream()
                .map(PostService::mapToDTO)
                .collect(toList());
    }

}
