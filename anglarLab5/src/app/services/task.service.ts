import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Task} from '../task';

@Injectable({providedIn: 'root'})
export class TaskService{
  private apiUrl = 'http://localhost:8080';
  constructor(private _httpClient: HttpClient) { }

  public getTasks(): Observable<Task[]> {
    return this._httpClient.get<Task[]>(`${this.apiUrl}/tasks`);
  }

  public deleteTask(taskId: number) {
    return this._httpClient.delete(`${this.apiUrl}/tasks/${taskId}`)
  }
}
