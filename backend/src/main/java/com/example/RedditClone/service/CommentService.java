package com.example.RedditClone.service;

import com.example.RedditClone.dto.CommentsDTO;
import com.example.RedditClone.exceptions.PostNotFoundException;
import com.example.RedditClone.mapper.CommentMapper;
import com.example.RedditClone.model.Comment;
import com.example.RedditClone.model.NotificationEmail;
import com.example.RedditClone.model.Post;
import com.example.RedditClone.model.User;
import com.example.RedditClone.repository.CommentRepository;
import com.example.RedditClone.repository.PostRepository;
import com.example.RedditClone.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.RedditClone.util.DateUtils.calcDuration;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;


    public void save(CommentsDTO commentsDTO){
        Post post =postRepository.findById(commentsDTO.getPostId())
                .orElseThrow(()-> new PostNotFoundException(commentsDTO.getPostId().toString()));
        Comment comment = commentMapper.map(commentsDTO, post, authService.getCurrentUser());
        commentRepository.save(comment);

        String message = mailContentBuilder.build(authService.getCurrentUser() + "postou um comentário no seu post" + POST_URL);
        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user){
        mailService.sendMail(new NotificationEmail(user.getUsername() + "comentou no seu post", user.getEmail(), message));
    }

    public List<CommentsDTO> getAllCommentsForPost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()-> new PostNotFoundException(postId.toString()));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDTO).collect(toList());
    }

    public List<CommentsDTO> getAllCommentsForUser(String userName){
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDTO)
                .collect(toList());
    }
}
