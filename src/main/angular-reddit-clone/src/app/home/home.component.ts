import { Component, OnInit } from '@angular/core';
import { faArrowUp, faArrowDown, faComments }from '@fortawesome/free-solid-svg-icons'
import { PostModel } from '../shared/post-model';
import { PostService } from '../shared/post.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})

export class HomeComponent implements OnInit {

  posts: PostModel[];

  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faComments = faComments;


  constructor(private postService: PostService) {    
  }

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
