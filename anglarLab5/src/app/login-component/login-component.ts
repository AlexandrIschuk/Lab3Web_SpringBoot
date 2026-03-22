import {ChangeDetectorRef, Component} from '@angular/core';
import {User} from '../interfaces/user';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {AuthService} from '../services/auth.service';
import {Router, RouterLink} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-login-component',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    NgIf,
    RouterLink
  ],
  templateUrl: './login-component.html',
  styleUrl: './login-component.css',
})
export class LoginComponent{
  protected loginForm: FormGroup;
  errorMessage = '';

  constructor(private fb: FormBuilder, private cd: ChangeDetectorRef,private router: Router, private authService: AuthService) {
    this.loginForm = this.fb.group({
      username: ['',[
        Validators.required
      ]],
      password: ['',[
        Validators.required
      ]],
    });
  }


  protected onSubmit() {
    if (this.loginForm.valid) {
      this.errorMessage = '';
      const user : User = this.loginForm.value;
      this.authService.login(user).subscribe({
        next: (response) => {
          sessionStorage.setItem('auth_token', response.token);
          sessionStorage.setItem('refresh_token', response.refreshToken);
          this.router.navigate(['/tasks']);
        },
        error: (error: HttpErrorResponse) => {
          if(error.status == 403){
            this.errorMessage = "Неправильный логин или пароль!"
            this.cd.detectChanges();
          }
          this.loginForm.patchValue({ password: '' });
        }
      }
      );
    }
  }
  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }
}
