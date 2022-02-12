import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CommentModel } from './comment.model';

@Injectable({
  providedIn: 'root'
})
export class CommentService {
  
  constructor(private http: HttpClient) { }

  getCommentsByPost(postId: number): Observable<CommentModel[]> {
    return this.http.get<CommentModel[]>('http://localhost:8080/api/comments/by-post/' + postId);
  }

  createComment(commentModel: CommentModel): Observable<CommentModel> {
    return this.http.post<CommentModel>('http://localhost:8080/api/comments/', commentModel);
  }

  getCommentsByUser(username: string) : Observable<CommentModel[]> {
    return this.http.get<CommentModel[]>('http://localhost:8080/api/comments/by-user/' + username);
  }

}
