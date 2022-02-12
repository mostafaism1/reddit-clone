package com.example.redditclone.repository;

import java.util.List;
import java.util.Optional;

import com.example.redditclone.model.Post;
import com.example.redditclone.model.User;
import com.example.redditclone.model.Vote;
import com.example.redditclone.model.VoteType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    Optional<Vote> findByUserAndPost(User user, Post post);

    List<Vote> findByPost(Post post);        

    int countByPostAndVoteType(Post post, VoteType voteType);

}
