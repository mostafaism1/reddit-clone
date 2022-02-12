package com.example.redditclone.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.model.Subreddit;
import com.example.redditclone.repository.SubredditRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class SubredditService {

    private final SubredditRepository subredditRepository;
    private final AuthService authService;

    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }

    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapSubredditToDto).collect(Collectors.toList());

    }

    public SubredditDto getSubreddit(long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(() -> new RedditException("No subreddit found with id = " + id));
        return mapSubredditToDto(subreddit);
    }

    private Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {
        return Subreddit.builder().name(subredditDto.getName()).description(subredditDto.getDescription())
                .user(authService.getCurrentUser()).createdAt(Instant.now()).build();
    }

    private SubredditDto mapSubredditToDto(Subreddit subreddit) {
        return SubredditDto.builder().id(subreddit.getId()).name(subreddit.getName())
                .description(subreddit.getDescription()).postCount(subreddit.getPosts().size()).build();
    }

	public SubredditDto getSubredditByName(String subredditName) {
		Subreddit subreddit = subredditRepository.findByName(subredditName)
                .orElseThrow(() -> new RedditException("No subreddit found with name = " + subredditName));
        return mapSubredditToDto(subreddit);
	}

}
