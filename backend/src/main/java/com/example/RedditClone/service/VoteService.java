package com.example.RedditClone.service;

import com.example.RedditClone.dto.VoteDTO;
import com.example.RedditClone.exceptions.PostNotFoundException;
import com.example.RedditClone.exceptions.SpringRedditException;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.Vote;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.RedditClone.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Transactional
    public void vote(VoteDTO voteDTO) {
        Post post = postRepository.findById(voteDTO.getPostId())
                .orElseThrow(() -> new PostNotFoundException("Post não encontrado com o id - " + voteDTO.getPostId()));
        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post, authService.getCurrentUser());
        if (voteByPostAndUser.isPresent() &&
                voteByPostAndUser.get().getVoteType()
                        .equals(voteDTO.getVoteType())) {
            throw new SpringRedditException("Voce já "
                    + voteDTO.getVoteType() + "'d para esse post");
        }
        if (UPVOTE.equals(voteDTO.getVoteType())) {
            post.setVoteCount(post.getVoteCount() + 1);
        } else {
            post.setVoteCount(post.getVoteCount() - 1);
        }
        voteRepository.save(mapToVote(voteDTO, post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDTO voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
