package com.example.redditclone.service;

import java.time.Instant;
import java.util.Optional;

import com.example.redditclone.dto.PostResponse;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.SubredditRepository;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VoteRepository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private SubredditRepository subredditRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthService authService;
    @Mock
    private VoteRepository voteRepository;
    @Mock
    private VoteService voteService;
    @Mock
    private CommentRepository commentRepository;

    PostService postService;

    User user;

    Subreddit subreddit;

    Post post;

    @BeforeEach
    public void setup() {
        postService = new PostService(postRepository, subredditRepository, userRepository, authService, voteRepository,
                voteService, commentRepository);

        user = new User(0L, "username1", "password", "email1@1email.com", Instant.now(), true);
        subreddit = new Subreddit(1L, "subreddit1", "subreddit1 description", Instant.now(), null, null);
    }

    @Test
    @DisplayName("Should find post by id")
    void getPostShouldFindPostById() {

        post = new Post(123L, "First Post", "http://url.site", "Test", 0, Instant.now(), user, subreddit);

        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.of(post));

        PostResponse expectedPostResponse = new PostResponse(123L, "First Post", "http://url.site", "Test", "Test User",
                "Test Subredit", 0, 0, "1 Hour Ago", false, false);
        PostResponse actualPostResponse = postService.getPost(123L);

        Assertions.assertThat(actualPostResponse.getId()).isEqualTo(expectedPostResponse.getId());
        Assertions.assertThat(actualPostResponse.getName()).isEqualTo(expectedPostResponse.getName());
    }

    @Test
    @DisplayName("Should not find post with non-existant id")
    void getPostShouldNotFindPostWithNonExistantId() {

        Mockito.when(postRepository.findById(123L)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> {
            postService.getPost(123L);
        }).isInstanceOf(RedditException.class).hasMessage("No post found with id = 123");
    }

}
