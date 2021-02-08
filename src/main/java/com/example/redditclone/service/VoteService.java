package com.example.redditclone.service;

import java.util.Optional;

import com.example.redditclone.dto.VoteDto;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;
import com.example.redditclone.model.Vote;
import com.example.redditclone.repository.PostRepository;
import com.example.redditclone.repository.VoteRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class VoteService {

    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    public void save(VoteDto voteDto) {

        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(() -> new RedditException("No post found with id = " + voteDto.getPostId()));

        User user = authService.getCurrentUser();

        Optional<Vote> voteByUserOnPost = voteRepository.findByUserAndPost(user, post);

        if (voteByUserOnPost.isPresent() && voteByUserOnPost.get().getVoteType() == voteDto.getVoteType()) {
            throw new RedditException("You have already voted " + voteDto.getVoteType() + " for this post");
        }

        Vote vote;
        if (voteByUserOnPost.isPresent()) {
            vote = voteByUserOnPost.get();
            vote.setVoteType(voteDto.getVoteType());
        } else {
            vote = mapVoteDtoToVote(voteDto, user, post);
        }

        int direction = vote.getVoteType().getDirection();

        post.setVoteCount(post.getVoteCount() + direction);

        postRepository.save(post);
        voteRepository.save(vote);
    }

    private Vote mapVoteDtoToVote(VoteDto voteDto, User user, Post post) {
        return Vote.builder().user(user).post(post).voteType(voteDto.getVoteType()).build();
    }

}
