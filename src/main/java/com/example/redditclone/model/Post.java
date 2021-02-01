package com.example.redditclone.model;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.springframework.data.annotation.Id;
import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Post Name cannot be empty or Null")
    private String name;

    @Nullable
    private String url;

    @Nullable
    @Lob
    private String description;

    private Integer voteCount = 0;

    private Instant createdAt;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(targetEntity = Subreddit.class, fetch = FetchType.LAZY)
    private Subreddit subreddit;

}
