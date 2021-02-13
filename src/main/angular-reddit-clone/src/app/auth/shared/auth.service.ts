import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable, throwError } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { LoginRequestPayload } from '../login/login.request.payload';
import { LoginResponsePayload } from '../login/login.response.payload';
import { SignupRequestPayload } from '../signup/SignupRequestPayload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  refreshTokenPayload = {
    refreshToken: this.getRefreshToken(),
    username: this.getUserName()
  }

  constructor(private httpClient: HttpClient, private localStorage: LocalStorageService) { }

  signup(signupRequestPayLoad: SignupRequestPayload): Observable<any> {
    return this.httpClient.post('http://localhost:8080/api/auth/signup', signupRequestPayLoad, { responseType: 'text' });

  }

  login(loginRequestPayload: LoginRequestPayload): Observable<LoginResponsePayload> {
    return this.httpClient.post<LoginResponsePayload>('http://localhost:8080/api/auth/login', loginRequestPayload)
      .pipe(
        map(data => {
          this.localStorage.store("authenticationToken", data.authenticationToken);
          this.localStorage.store("username", data.username);
          this.localStorage.store("expiresAt", data.expiresAt);
          this.localStorage.store("refreshToken", data.refreshToken);

          return data;
        }));
  }

  getJwtToken() {
    return this.localStorage.retrieve('authenticationToken');
  }

  refreshToken() {
    return this.httpClient.post<LoginResponsePayload>('http://localhost:8080/api/auth/refresh/token',
      this.refreshTokenPayload)
      .pipe(tap(response => {
        this.localStorage.clear('authenticationToken');
        this.localStorage.clear('expiresAt');

        this.localStorage.store('authenticationToken',
          response.authenticationToken);
        this.localStorage.store('expiresAt', response.expiresAt);
      }));
  }


  getUserName() {
    return this.localStorage.retrieve('username');
  }

  getRefreshToken() {
    return this.localStorage.retrieve('refreshToken');
  }

  isLoggedIn(): boolean {
    return this.getJwtToken() !== null;
  }

  logout() {

    this.httpClient.post('http://localhost:8080/api/auth/logout', this.refreshTokenPayload,
      { responseType: 'text' })
      .subscribe(data => {
        console.log(data);
      }, error => {
        throwError(error);
      });

    this.localStorage.clear('authenticationToken');
    this.localStorage.clear('username');
    this.localStorage.clear('refreshToken');
    this.localStorage.clear('expiresAt');
  }

}


