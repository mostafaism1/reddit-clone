package com.example.redditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.example.redditclone.dto.CommentDto;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.Comment;
import com.example.redditclone.model.NotificationEmail;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.UserRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {

        private final CommentRepository commentRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;
        private final AuthService authService;
        private final MailContentBuilder mailContentBuilder;
        private final MailService mailService;
    
    public CommentDto save(CommentDto commentDto) {

                Post post = postRepository.findById(commentDto.getPostId()).orElseThrow(
                                () -> new RedditException("No post found with id = " + commentDto.getPostId()));

                User user = authService.getCurrentUser();

                Comment comment = Comment.builder().text(commentDto.getText()).post(post).user(user)
                                .createdAt(Instant.now()).build();

                comment = commentRepository.save(comment);

                String message = mailContentBuilder.build(
                                comment.getUser().getUsername() + " posted a comment on your post. " + post.getUrl());

                mailService.sendMail(new NotificationEmail(
                                comment.getUser().getUsername() + " commented on your post. " + post.getUrl(),
                                post.getUser().getUsername(), message));

                return mapCommentToDto(comment);
        }

        @Transactional(readOnly = true)
        public List<CommentDto> getAllCommentsForPost(long postId) {
                Post post = postRepository.findById(postId)
                                .orElseThrow(() -> new RedditException("No post found with id = " + postId));

                List<Comment> comments = commentRepository.findByPost(post);
                return mapCommentsToDto(comments);
        }

        @Transactional(readOnly = true)
        public List<CommentDto> getCommentsByUsername(String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RedditException("No user found with username = " + username));
                List<Comment> comments = commentRepository.findByUser(user);
                return mapCommentsToDto(comments);
        }

        private List<CommentDto> mapCommentsToDto(List<Comment> comments) {
                return comments.stream().map(this::mapCommentToDto).collect(Collectors.toList());
        }

        private CommentDto mapCommentToDto(Comment comment) {
                return CommentDto.builder().id(comment.getId()).postId(comment.getPost().getId())
                                .text(comment.getText()).createdAt(TimeAgo.using(comment.getCreatedAt().toEpochMilli()))
                                .username(comment.getUser().getUsername()).build();
        }

}
