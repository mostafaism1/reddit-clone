package com.example.redditclone.repository;

import java.util.List;

import com.example.redditclone.model.Post;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    
    List<Post> OrderByCreatedAtDesc(); 

    List<Post> findBySubredditOrderByCreatedAtDesc(Subreddit subreddit);

    List<Post> findByUserOrderByCreatedAtDesc(User user);

}
