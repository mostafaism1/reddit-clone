import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';
import { SubredditModel } from 'src/app/subreddit/subreddit-model';
import { SubredditService } from 'src/app/subreddit/subreddit.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.css']
})
export class CreatePostComponent implements OnInit {

  createPostForm: FormGroup;
  postModel: PostModel;
  subreddits: SubredditModel[];

  constructor(private router: Router, private postService: PostService, private subredditService: SubredditService) {
    this.createPostForm = new FormGroup({
      name: new FormControl('', Validators.required),
      subredditName: new FormControl('', Validators.required),
      url: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required)
    });

    this.postModel = new PostModel();

    this.subreddits = [];
  }

  ngOnInit(): void {
    this.subredditService.getAllSubreddits().subscribe(
      subreddits => {
        this.subreddits = subreddits;
      },
      error => {
        throwError(error);
      });
  }

  createPost() {
    this.postModel.name = this.createPostForm.get('name')?.value;
    this.postModel.subredditName = this.createPostForm.get('subredditName')?.value;
    this.postModel.url = this.createPostForm.get('url')?.value;
    this.postModel.description = this.createPostForm.get('description')?.value;

    this.postService.createPost(this.postModel).subscribe(
      (data) => {
        this.router.navigateByUrl('/');
      },
      (error) => {
        throwError(error);
      }
    );
  }

  discardPost() {
    this.router.navigateByUrl('/');
  }

}
