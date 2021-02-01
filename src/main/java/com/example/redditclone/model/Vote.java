package com.example.redditclone.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private VoteType voteType;

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
}