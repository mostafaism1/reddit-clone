package com.example.redditclone.controller;

import java.util.List;

import com.example.redditclone.dto.SubredditDto;
import com.example.redditclone.service.SubredditService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api/subreddits")
@AllArgsConstructor
public class SubredditController {

    private final SubredditService subredditService;

    @PostMapping
    public ResponseEntity<SubredditDto> createSubreddit(@RequestBody SubredditDto subredditDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));
    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits() {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }

    @GetMapping("/by-name/{subredditName}")
    public ResponseEntity<SubredditDto> getSubredditByName(@PathVariable String subredditName) {
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubredditByName(subredditName));
    }

}
