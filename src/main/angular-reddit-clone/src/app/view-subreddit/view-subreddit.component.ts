import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostModel } from '../shared/post-model';
import { PostService } from '../shared/post.service';
import { SubredditModel } from '../subreddit/subreddit-model';
import { SubredditService } from '../subreddit/subreddit.service';

@Component({
  selector: 'app-view-subreddit',
  templateUrl: './view-subreddit.component.html',
  styleUrls: ['./view-subreddit.component.css']
})
export class ViewSubredditComponent implements OnInit {
  
  subredditName: string;
  subreddit: SubredditModel;
  posts: PostModel[];

  constructor(private activatedRoute: ActivatedRoute, private postService: PostService, private subredditService: SubredditService) { 
    this.subreddit = new SubredditModel();
  }

  ngOnInit(): void {
    this.subredditName = this.activatedRoute.snapshot.params.subredditName;
    this.subredditService.getSubredditByName(this.subredditName).subscribe(subreddit => {
      this.subreddit = subreddit;
      this.postService.getPostsBySubreddit(this.subreddit.id).subscribe(posts => this.posts = posts);    
    });    
  }

}
