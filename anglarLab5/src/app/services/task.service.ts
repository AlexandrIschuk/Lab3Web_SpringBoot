import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Task} from '../interfaces/task';
import {Environment} from './eviroment';

@Injectable({providedIn: 'root'})
export class TaskService{
  private taskInit: Task = {id: 0, title: " ",status: " ",createdBy: 0, createdAt: new Date()};
  constructor(private _httpClient: HttpClient, private environment: Environment) { }

  public getTasks(): Observable<Task[]> {
    return this._httpClient.get<Task[]>(`${this.environment.getUrl()}/tasks`);
  }

  public getTask(taskId: String | null): Observable<Task> {
    return this._httpClient.get<Task>(`${this.environment.getUrl()}/tasks/${taskId}`);
  }

  public deleteTask(taskId: number) {
    return this._httpClient.delete(`${this.environment.getUrl()}/tasks/${taskId}`)
  }
  public setTask(task:Task){
    this.taskInit = task;
  }

  public clearTask(){
    this.taskInit = {id: 0, title: " ",status: " ",createdBy: 0, createdAt: new Date()};
  }

  public updateTask(task: Task) {
    return this._httpClient.put(`${this.environment.getUrl()}/tasks/${task.id}`,task)
  }
  public createTask(task: Task) {
    return this._httpClient.post(`${this.environment.getUrl()}/tasks`,task)
  }
}
