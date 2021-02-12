import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { throwError } from 'rxjs';
import { SubredditModel } from '../subreddit-model';
import { SubredditService } from '../subreddit.service';

@Component({
  selector: 'app-create-subreddit',
  templateUrl: './create-subreddit.component.html',
  styleUrls: ['./create-subreddit.component.css']
})
export class CreateSubredditComponent implements OnInit {
  createSubredditForm: FormGroup = new FormGroup({
    title: new FormControl('', Validators.required),
    description: new FormControl('', Validators.required)
  });

  subredditModel: SubredditModel = new SubredditModel();

  constructor(private router: Router, private subredditService: SubredditService,) {
  }

  ngOnInit(): void {
  }

  createSubreddit() {
    this.subredditModel.name = this.createSubredditForm.get('title')?.value;
    this.subredditModel.description = this.createSubredditForm.get('description')?.value;

    this.subredditService.createSubreddit(this.subredditModel).subscribe(
      data => {
        this.router.navigateByUrl('/list-subreddits');
      },
      error => {
        throwError(error);
      });
  }

  discard() {
    this.router.navigateByUrl('/');
  }

}
