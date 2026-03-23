import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {TaskService} from '../services/task.service';
import {map, Observable} from 'rxjs';
import {Task} from '../interfaces/task';
import {AsyncPipe} from '@angular/common';
import {RouterLink} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';
import {TaskComponent} from '../task-component/task-component';

@Component({
  selector: 'app-task-list-component',
  imports: [
    AsyncPipe,
    RouterLink,
    TaskComponent
  ],
  templateUrl: './task-list-component.html',
  styleUrl: './task-list-component.css',
})
export class TaskListComponent implements OnInit{

    public tasks$?: Observable<Task[]>;
  protected errorMessage = '';

    constructor(private taskService: TaskService, private cd: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.tasks$ = this.taskService.getTasks();
    }

  protected deleteTask(id: number) {
    this.taskService.deleteTask(id).subscribe({
      next: () => {
        if (this.tasks$) {
          this.tasks$ = this.tasks$.pipe(
            map(tasks => tasks.filter(t => t.id !== id))
          );
        }
        this.cd.detectChanges();
        this.errorMessage = '';
      },
      error: (error: HttpErrorResponse) => {
        if(error.status == 403){
          this.errorMessage = "Вы не являетесь администратором!"
          this.cd.detectChanges();
        }
      }
    });
  }

  protected logout() {
    sessionStorage.clear();
  }
}
