package com.example.redditclone.service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.exception.RedditException;
import com.example.redditclone.mapper.subredditMapper;
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
    private final subredditMapper subredditMapper;

    public SubredditDto save(SubredditDto subredditDto) {
        Subreddit subreddit = subredditRepository.save(subredditMapper.mapDtoToSubreddit(subredditDto));
        subredditDto.setId(subreddit.getId());
        return subredditDto;
    }    


    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(subredditMapper::mapSubredditToDto).collect(Collectors.toList());

    }


	public SubredditDto getSubreddit(long id) {
        Subreddit subreddit = subredditRepository.findById(id)
            .orElseThrow(() -> new RedditException("No subreddit found with id = " + id));
        return subredditMapper.mapSubredditToDto(subreddit);
	}

}
