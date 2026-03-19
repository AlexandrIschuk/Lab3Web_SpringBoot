import {HttpErrorResponse, HttpInterceptorFn} from '@angular/common/http';
import {catchError, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {inject} from '@angular/core';



export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const credentials = sessionStorage.getItem("auth_token");
  if (credentials) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${credentials}`)
    });
    return next(authReq);
  }

  return next(req);
};
