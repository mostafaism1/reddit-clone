package com.example.redditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.example.redditclone.dto.PostRequest;
import com.example.redditclone.dto.PostResponse;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;
import com.example.redditclone.model.VoteType;
import com.example.redditclone.repository.CommentRepository;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.SubredditRepository;
import com.example.redditclone.repository.UserRepository;
import com.example.redditclone.repository.VoteRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubredditRepository subredditRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final VoteRepository voteRepository;
    private final VoteService voteService;
    private final CommentRepository commentRepository;

    public PostResponse save(PostRequest postRequest) {
        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName()).orElseThrow(
                () -> new RedditException("No subreddit found with name = " + postRequest.getSubredditName()));

        User user = authService.getCurrentUser();

        Post post = Post.builder().name(postRequest.getName()).url(postRequest.getUrl())
                .description(postRequest.getDescription()).subreddit(subreddit).user(user).createdAt(Instant.now())
                .voteCount(0).build();

        postRepository.save(post);

        return mapPostToPostResponse(post);

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.OrderByCreatedAtDesc().stream().map(this::mapPostToPostResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RedditException("No post found with id = " + id));
        ;
        return mapPostToPostResponse(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("No subreddit found with id = " + id));
        List<Post> posts = postRepository.findBySubredditOrderByCreatedAtDesc(subreddit);

        return posts.stream().map(this::mapPostToPostResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RedditException("No user found with username = " + username));

        List<Post> posts = postRepository.findByUserOrderByCreatedAtDesc(user);

        return posts.stream().map(this::mapPostToPostResponse).collect(Collectors.toList());

    }

    private PostResponse mapPostToPostResponse(Post post) {
        return PostResponse.builder()
            .id(post.getId())
            .name(post.getName())
            .url(post.getUrl())
            .description(post.getDescription())
            .userName(post.getUser().getUsername())
            .subredditName(post.getSubreddit().getName()).voteCount(voteRepository.findByPost(post).size())
            .commentCount(commentRepository.findByPostOrderByCreatedAtDesc(post).size())
            .duration(TimeAgo.using(post.getCreatedAt().toEpochMilli()))
            .upVoted(voteService.isPostVotedByCurrentUser(post, VoteType.UPVOTE))
            .downVoted(voteService.isPostVotedByCurrentUser(post, VoteType.DOWNVOTE))
            .voteCount(voteService.getNetVoteCount(post))
        .build();
    }

}
