package com.example.redditclone.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDto {
    
    private long id;

    private long postId;
    
    private String text;

    private Instant createdAt;    
    
    private String username;
    
}
