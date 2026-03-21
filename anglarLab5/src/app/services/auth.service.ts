import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {User} from '../interfaces/user';
import {Environment} from './eviroment';
import {Observable} from 'rxjs';
import {AuthResponse} from '../interfaces/auth.response';

@Injectable({providedIn: 'root'})
export class AuthService{
  constructor(private _httpClient: HttpClient, private environment: Environment) { }
  public login(user: User):Observable<AuthResponse>{
    return this._httpClient.post<AuthResponse>(`${this.environment.getUrl()}/auth/login`, user);
  }

  public refreshToken():Observable<AuthResponse>{
    return this._httpClient.post<AuthResponse>(`${this.environment.getUrl()}/auth/refresh`,null, {withCredentials: true});
  }




}
