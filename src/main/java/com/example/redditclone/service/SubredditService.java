package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.repository.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;

    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(mapSubredditDto(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    private Subreddit mapSubredditDto(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName()).description(subredditDto.getDescription()).build();
    }

    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());

    }

    private SubredditDto mapToDto(Subreddit subreddit) {
        return SubredditDto.builder().id(subreddit.getId()).name(subreddit.getName())
                .description(subreddit.getDescription()).postCount(subreddit.getPosts().size()).build();
    }

}
