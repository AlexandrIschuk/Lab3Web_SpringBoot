import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';



export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const credentials = sessionStorage.getItem("auth_token");
  if (credentials) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${credentials}`)
    });
    return next(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if(error.status == 401){
          sessionStorage.clear();
          authService.refreshToken().subscribe((response) => {
            sessionStorage.setItem('auth_token', response.token);
          });

        }
        return throwError(error);
      })
    );
  }

  return next(req);
};
