import { Component, Input, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp } from '@fortawesome/free-solid-svg-icons';
import { ToastrService } from 'ngx-toastr';
import { throwError } from 'rxjs';
import { AuthService } from 'src/app/auth/shared/auth.service';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';
import { VoteType } from '../vote-type';
import { VoteModel } from '../vote.model';
import { VoteService } from '../vote.service';

@Component({
  selector: 'app-vote-button',
  templateUrl: './vote-button.component.html',
  styleUrls: ['./vote-button.component.css']
})
export class VoteButtonComponent implements OnInit {

  @Input() post: PostModel;
  voteModel: VoteModel;  
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;  

  constructor(private voteService: VoteService, private postService: PostService, private toastrService: ToastrService) {
    this.voteModel = {
      voteType: undefined,
      postId: undefined
    }
   }

  ngOnInit(): void {    
  }

  upvotePost() {
    this.voteModel.voteType = VoteType.UPVOTE;
    this.vote();
  }

  downvotePost() {
    this.voteModel.voteType = VoteType.DOWNVOTE;
    this.vote();
  }  

  vote() {
    this.voteModel.postId = this.post.id;
    this.voteService.vote(this.voteModel).subscribe(() => {
      this.updateVoteDetails();
    }, error => {
      this.toastrService.error(error.error.message);
      throwError(error);
    });
  }

  updateVoteDetails() {
    this.postService.getPost(this.post.id).subscribe(
      (post) => {
        this.post = post;
      })
  }
}
