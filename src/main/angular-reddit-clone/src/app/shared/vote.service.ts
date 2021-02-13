import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { VoteModel } from './vote.model';

@Injectable({
  providedIn: 'root'
})
export class VoteService {

  constructor(private http: HttpClient) { }

  vote(voteModel: VoteModel): Observable<any> {
    return this.http.post<any>('http://localhost:8080/api/votes/', voteModel);
  }
}
