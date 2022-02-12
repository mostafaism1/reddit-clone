import { Component, OnInit } from '@angular/core';
import { throwError } from 'rxjs';
import { SubredditModel } from '../subreddit-model';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-list-subreddits',
  templateUrl: './list-subreddits.component.html',
  styleUrls: ['./list-subreddits.component.css']
})
export class ListSubredditsComponent implements OnInit {
  subreddits: SubredditModel[];

  constructor(private subredditService: SubredditService) { }

  ngOnInit(): void {
    this.subredditService.getAllSubreddits().subscribe(
      subreddits => {
        this.subreddits = subreddits;
      },
      error => {
        throwError(error);
      });
  }

}
