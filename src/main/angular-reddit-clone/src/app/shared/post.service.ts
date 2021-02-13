import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PostModel } from './post-model';

@Injectable({
  providedIn: 'root'
})
export class PostService {  

  constructor(private http: HttpClient) { }

  getAllPosts(): Observable<Array<PostModel>> {
    return this.http.get<Array<PostModel>>('http://localhost:8080/api/posts/');
  }

  createPost(postModel: PostModel) {
    return this.http.post<PostModel>('http://localhost:8080/api/posts/', postModel);
  }

  getPost(postId: number): Observable<PostModel> {
    return this.http.get<PostModel>('http://localhost:8080/api/posts/' + postId);
  }

  getPostsByUser(username: string) : Observable<PostModel[]> {
    return this.http.get<PostModel[]>('http://localhost:8080/api/posts/by-user/' + username);
  }

  getPostsBySubreddit(subredditId: number) : Observable<PostModel[]> {
    return this.http.get<PostModel[]>('http://localhost:8080/api/posts/by-subreddit/' + subredditId);
  }
  
}
