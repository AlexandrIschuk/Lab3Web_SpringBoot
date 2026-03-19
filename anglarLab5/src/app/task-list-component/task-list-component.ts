import {ChangeDetectorRef, Component, OnInit} from "@angular/core";
import {TaskService} from '../services/task.service';
import {map, Observable} from 'rxjs';
import {Task} from '../interfaces/task';
import {AsyncPipe, DatePipe, NgIf} from '@angular/common';
import {RouterLink} from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-task-list-component',
  imports: [
    AsyncPipe,
    DatePipe,
    RouterLink,
    NgIf
  ],
  templateUrl: './task-list-component.html',
  styleUrl: './task-list-component.css',
})
export class TaskListComponent implements OnInit{

    public tasks$?: Observable<Task[]>;
  private router: any;
  protected errorMessage = '';

    constructor(private taskService: TaskService, private cd: ChangeDetectorRef) { }

    ngOnInit(): void {
        this.tasks$ = this.taskService.getTasks();
    }

  protected deleteTask(task: Task) {
    this.taskService.deleteTask(task.id).subscribe({
      next: (response) => {
        if (this.tasks$) {
          this.tasks$ = this.tasks$.pipe(
            map(tasks => tasks.filter(t => t.id !== task.id))
          );
        }
        this.cd.detectChanges();
        this.errorMessage = ''; // Очищаем сообщение об ошибке
      },
      error: (error: HttpErrorResponse) => {
        if(error.status == 403){
          this.errorMessage = "Вы не являетесь администратором!"
          this.cd.detectChanges();
        }
      }
    });
  }
  protected setTask(task: Task){
      this.taskService.setTask(task);
  }

  protected logout() {
    sessionStorage.clear();
  }
}
