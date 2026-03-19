import {Component, OnInit} from '@angular/core';
import {Task} from '../interfaces/task';
import {Location, NgForOf} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {TaskService} from '../services/task.service';

@Component({
  selector: 'app-edit-task-component',
  imports: [
    FormsModule,
    NgForOf
  ],
  templateUrl: './edit-task-component.html',
  styleUrl: './edit-task-component.css',
})
export class EditTaskComponent implements OnInit{
  public task$ : Task;

  statuses = [
    { value: 'OPEN', label: 'Открыта' },
    { value: 'IN_PROGRESS', label: 'В работе' },
    { value: 'DONE', label: 'Выполнена' },
    { value: 'CLOSED', label: 'Завершена' }
  ];


  constructor(private taskService: TaskService, private location: Location) {
    this.task$ = this.taskService.getTask();
  }


  ngOnInit(): void {
        this.task$ = this.taskService.getTask();
    }

  protected onCancel() {
    this.location.back();
    this.taskService.clearTask();
  }

  protected update(task: Task) {
    if(task.id == 0){
      this.taskService.createTask(task).subscribe(() => this.onCancel());
      this.taskService.clearTask();
    }else {
      this.taskService.updateTask(task).subscribe(() => this.onCancel());
      this.taskService.clearTask();
    }

  }
}
