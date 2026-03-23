import {ChangeDetectorRef, Component} from '@angular/core';
import {Location, NgIf} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {User} from '../interfaces/user';
import {HttpErrorResponse} from '@angular/common/http';
import {UserService} from '../services/user.service';

@Component({
  selector: 'app-registration-user-component',
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './registration-user-component.html',
  styleUrl: './registration-user-component.css',
})
export class RegistrationUserComponent {
  errorMessage = '';
  protected registerForm: FormGroup;
  successMessage = '';
  constructor(private location: Location, private cd: ChangeDetectorRef,private fb: FormBuilder,private userService: UserService) {
    this.registerForm = this.fb.group({
      username: ['',[
        Validators.required
      ]],
      password: ['',[
        Validators.required
      ]],
      confirmPassword: ['',[
        Validators.required
      ]]
    });
  }

  protected onCancel() {
    this.location.back();
  }

  protected onSubmit() {
    if (this.registerForm.valid) {
      if (this.password?.value == this.confirmPassword?.value) {
        this.errorMessage = '';
        const user: User = this.registerForm.value;
        this.userService.registration(user).subscribe({
            next: () => {
              this.successMessage = 'Регистрация прошла успешно! Перенаправление на страницу входа...';
              this.cd.detectChanges();
              setTimeout(() => {
                this.onCancel();
              }, 2000);
            },
            error: (error: HttpErrorResponse) => {
              if (error.status == 409) {
                this.errorMessage = "Пользователь уже существует!"
                this.cd.detectChanges();
              }
              this.registerForm.patchValue({username: ''});
              this.registerForm.patchValue({password: ''});
            }
          }
        );
      } else {
        this.errorMessage = "Пароли должны совпадать"
        this.cd.detectChanges();
      }
    }
  }
  get username() { return this.registerForm.get('username'); }
  get password() { return this.registerForm.get('password'); }
  get confirmPassword() { return this.registerForm.get('confirmPassword'); }
}
