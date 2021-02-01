package com.example.redditclone.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Comment cannot be empty or Null")
    private String text;

    private Instant createdAt;

    @ManyToOne(targetEntity = Post.class, fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;
}