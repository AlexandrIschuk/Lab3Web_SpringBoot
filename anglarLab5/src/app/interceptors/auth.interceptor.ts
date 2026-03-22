import {HttpErrorResponse, HttpInterceptorFn, HttpRequest} from '@angular/common/http';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth.service';
import { catchError, switchMap, throwError } from 'rxjs';



export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const credentials = sessionStorage.getItem("auth_token");
  if (credentials) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${credentials}`)
    });
    return next(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          const refreshToken = sessionStorage.getItem('refresh_token');

          return authService.refreshToken(refreshToken).pipe(
            switchMap((response) => {
              sessionStorage.setItem('auth_token', response.token);

              const newAuthReq = req.clone({
                headers: req.headers.set('Authorization', `Bearer ${response.token}`)
              });

              return next(newAuthReq);
            })
          );
        }
        return throwError(error);
      })
    );
  }
  return next(req);
};
