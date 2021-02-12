import { Component, OnInit } from '@angular/core';
import { faArrowDown, faArrowUp, faComments } from '@fortawesome/free-solid-svg-icons';
import { PostModel } from '../post-model';
import { PostService } from '../post.service';

@Component({
  selector: 'app-post-tile',
  templateUrl: './post-tile.component.html',
  styleUrls: ['./post-tile.component.css']
})
export class PostTileComponent implements OnInit {

  posts: PostModel[];

  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faComments = faComments;

  constructor(private postService: PostService) { }

  ngOnInit(): void {
    this.postService.getAllPosts().subscribe(posts => this.posts = posts);
  }

  upvotePost() {

  }

  downvotePost() {

  }

  goToPost(postId: number) {

  }

}
