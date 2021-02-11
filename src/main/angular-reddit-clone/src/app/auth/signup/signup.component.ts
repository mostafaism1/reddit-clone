import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../shared/auth.service';
import { SignupRequestPayload } from './SignupRequestPayload';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  signupForm: FormGroup;
  signupRequestPayLoad: SignupRequestPayload;

  constructor(private authService: AuthService) {
    this.signupRequestPayLoad = {
      email: "",
      username: "",
      password: ""
    }
  }

  ngOnInit(): void {
    this.signupForm = new FormGroup({
      username: new FormControl('', Validators.required),
      email: new FormControl('', [Validators.required, Validators.email]),
      password: new FormControl('', Validators.required)
    });
  }

  signup() {
    this.signupRequestPayLoad.email = this.signupForm.get("email")?.value;
    this.signupRequestPayLoad.username = this.signupForm.get("username")?.value;
    this.signupRequestPayLoad.password = this.signupForm.get("password")?.value;

    this.authService.signup(this.signupRequestPayLoad)
    .subscribe(data => {
      console.log(data);
    });

  }

}
