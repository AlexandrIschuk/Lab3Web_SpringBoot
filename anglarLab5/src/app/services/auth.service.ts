import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {User} from '../user';

@Injectable({providedIn: 'root'})
export class AuthService{
  constructor(private _httpClient: HttpClient) { }
  public login(){
    //return this._httpClient.post('http://localhost:8080/login');
  }
}
