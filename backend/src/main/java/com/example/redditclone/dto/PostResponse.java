package com.example.redditclone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

    private long id;

    private String name;

    private String url;

    private String description;

    private String userName;

    private String subredditName;

    private int voteCount;

    private int commentCount;

    private String duration;

    private boolean upVoted;

    private boolean downVoted;
}
