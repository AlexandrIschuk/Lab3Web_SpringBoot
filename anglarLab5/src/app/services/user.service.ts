import {User} from '../interfaces/user';
import {HttpClient} from '@angular/common/http';
import {Environment} from './eviroment';
import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class UserService{
  constructor(private _httpClient: HttpClient, private environment: Environment ) {
  }
  public registration(user: User){
    return this._httpClient.post(`${this.environment.getUrl()}/users/register`, user);
  }
}
