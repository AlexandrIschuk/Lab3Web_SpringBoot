import {Component, Input, OnInit} from '@angular/core';
import {Task} from '../interfaces/task';
import {Location, NgForOf} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TaskService} from '../services/task.service';

@Component({
  selector: 'app-edit-task-component',
  imports: [
    FormsModule,
    NgForOf,
    ReactiveFormsModule
  ],
  templateUrl: './edit-task-component.html',
  styleUrl: './edit-task-component.css',
})
export class EditTaskComponent implements OnInit{
  task$: Task;
  taskForm: FormGroup;
  taskId: number;

  statuses = [
    { value: 'OPEN', label: 'Открыта' },
    { value: 'IN_PROGRESS', label: 'В работе' },
    { value: 'DONE', label: 'Выполнена' },
    { value: 'CLOSED', label: 'Завершена' }
  ];


  constructor(private taskService: TaskService, private location: Location,private fb: FormBuilder) {
    this.task$ = this.taskService.getTask();
    this.taskForm = this.fb.group({
      title: [''],
      status: ['']
    });
    this.taskId = this.task$?.id;
  }

  ngOnInit() {
    if (this.task$) {
      this.taskForm.patchValue({
        title: this.task$.title,
        status: this.task$.status
      });
    }
  }
  onSubmit() {
    if (this.taskForm.valid) {
      const updatedTask: Task = {
        ...this.task$, // сохраняем остальные поля
        ...this.taskForm.value // перезаписываем измененные
      };
      this.update(updatedTask);
    }

  }


  protected onCancel() {
    this.location.back();
    this.taskService.clearTask();
  }

  protected update(task: Task) {
    if(task.id == 0){
      this.taskService.createTask(task).subscribe(() => this.onCancel());
    }else {
      this.taskService.updateTask(task).subscribe(() => this.onCancel());
    }

  }
}
