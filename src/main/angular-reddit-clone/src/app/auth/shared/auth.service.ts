import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LocalStorageService } from 'ngx-webstorage';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LoginRequestPayload } from '../login/login.request.payload';
import { LoginResponsePayload } from '../login/login.response.payload';
import { SignupRequestPayload } from '../signup/SignupRequestPayload';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

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
}


