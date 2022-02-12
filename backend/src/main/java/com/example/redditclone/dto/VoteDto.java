package com.example.redditclone.dto;

import com.example.redditclone.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {

    private VoteType voteType;

    private long postId;

}
