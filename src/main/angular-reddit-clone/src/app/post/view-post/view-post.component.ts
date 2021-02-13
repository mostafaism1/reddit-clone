import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { throwError } from 'rxjs';
import { CommentModel } from 'src/app/comment/comment.model';
import { CommentService } from 'src/app/comment/comment.service';
import { PostModel } from 'src/app/shared/post-model';
import { PostService } from 'src/app/shared/post.service';

@Component({
  selector: 'app-view-post',
  templateUrl: './view-post.component.html',
  styleUrls: ['./view-post.component.css']
})
export class ViewPostComponent implements OnInit {

  postId: number;
  post: PostModel;
  commentForm: FormGroup;
  commentModel: CommentModel;
  comments: CommentModel[];

  constructor(private postService: PostService, private activatedRoute: ActivatedRoute, private commentService: CommentService) {
    this.post = new PostModel();
    this.commentModel = new CommentModel;
    this.comments = []
    this.commentForm = new FormGroup({ text: new FormControl('', Validators.required) });    
  }

  ngOnInit(): void {
    this.postId = this.activatedRoute.snapshot.params.id;
    this.getPostById(this.postId);

    this.getCommentsForPost();
  }

  getCommentsForPost() {
    this.commentService.getCommentsByPost(this.postId).subscribe(
      (comments) => {
        this.comments = comments;
      },
      (error) => {
        throwError(error);
      });
  }

  getPostById(postId: number) {
    this.postService.getPost(this.postId).subscribe(
      (post) => {
        this.post = post;
      },
      (error) => {
        throwError(error);
      }
    );
  }

  postComment() {
    this.commentModel.text = this.commentForm.get('text')?.value;
    this.commentModel.postId = this.postId;

    this.commentService.createComment(this.commentModel).subscribe(
      (data) => {
        this.commentForm.get('text')?.setValue('');

        this.getCommentsForPost();
      },
      (error) => {
        throwError(error);
      }
    );
  }

}
