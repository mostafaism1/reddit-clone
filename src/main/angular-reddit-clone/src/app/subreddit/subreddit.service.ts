import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SubredditModel } from './subreddit-model';

@Injectable({
  providedIn: 'root'
})
export class SubredditService {  

  constructor(private http: HttpClient) { }

  getAllSubreddits(): Observable<SubredditModel[]> {
    return this.http.get<SubredditModel[]>('http://localhost:8080/api/subreddits');
  }

  createSubreddit(subredditModel: SubredditModel) : Observable<SubredditModel>{
    return this.http.post<SubredditModel>('http://localhost:8080/api/subreddits', subredditModel);
  }

  getSubredditByName(subredditName: string) : Observable<SubredditModel> {
    return this.http.get<SubredditModel>('http://localhost:8080/api/subreddits/by-name/' + subredditName);
  }  
}
