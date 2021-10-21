package com.example.RedditClone.service;

import com.example.RedditClone.dto.PostResponse;
import com.example.RedditClone.dto.SubredditDTO;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.exceptions.SubredditNotFoundException;
import com.example.RedditClone.mapper.SubredditMapper;
import com.example.RedditClone.model.Subreddit;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.RedditClone.util.Constants.SUBREDDIT_NOT_FOUND_WITH_ID;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class SubredditService {
    private final SubredditRepository subredditRepository;
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDTO save(SubredditDTO subredditDto) {
        Subreddit save = subredditRepository.save(subredditMapper.mapDTOToSubreddit(subredditDto));
        subredditDto.setId(save.getId());
        return subredditDto;
    }

    public List<SubredditDTO> getAll(){
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToDTO)
                .collect(toList());
    }


    public SubredditDTO getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new SpringRedditException("não subreddit encontrado com o id: - " + id));
        return subredditMapper.mapSubredditToDTO(subreddit);
    }

}
