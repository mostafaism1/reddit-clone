package com.example.redditclone.controller;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.service.SubredditService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api/subreddit")
@AllArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

}
