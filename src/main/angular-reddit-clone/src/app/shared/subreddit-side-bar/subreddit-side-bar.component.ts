import { Component, OnInit } from '@angular/core';
import { SubredditModel } from 'src/app/subreddit/subreddit-model';
import { SubredditService } from 'src/app/subreddit/subreddit.service';

@Component({
  selector: 'app-subreddit-side-bar',
  templateUrl: './subreddit-side-bar.component.html',
  styleUrls: ['./subreddit-side-bar.component.css']
})
export class SubredditSideBarComponent implements OnInit {

  subreddits: SubredditModel[];
  displayViewAll: boolean;

  constructor(private subredditService: SubredditService) { }

  ngOnInit(): void {
    this.subredditService.getAllSubreddits().subscribe(subreddits => {
      if (subreddits.length > 3) {
        this.subreddits = subreddits.splice(0, 3);
        this.displayViewAll = true;

      } else {
        this.subreddits = subreddits;
      }
    });
  }

}
