import {Injectable} from '@angular/core';

@Injectable({providedIn: 'root'})
export class Environment {
  private apiUrl = 'http://localhost:8080';

  public getUrl(){
    return this.apiUrl;
  }
}
