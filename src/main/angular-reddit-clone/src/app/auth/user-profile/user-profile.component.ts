import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommentModel } from 'src/app/comment/comment.model';
import { CommentService } from 'src/app/comment/comment.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  username: string;
  posts: PostModel[];
  comments: CommentModel[];
  

  constructor(private postService: PostService, private commentService: CommentService, activatedRoute: ActivatedRoute) {
    this.username = activatedRoute.snapshot.params.username;
    this.postService.getPostsByUser(this.username).subscribe(posts => this.posts = posts);

    this.commentService.getCommentsByUser(this.username).subscribe(comments => this.comments = comments);
   }

  ngOnInit(): void {
  }

}
