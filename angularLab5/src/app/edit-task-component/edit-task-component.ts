import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {Task} from '../interfaces/task';
import {AsyncPipe, Location, NgForOf, NgIf} from '@angular/common';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from '@angular/forms';
import {TaskService} from '../services/task.service';
import { ActivatedRoute } from '@angular/router';
import {HttpErrorResponse} from '@angular/common/http';

@Component({
  selector: 'app-edit-task-component',
  imports: [
    FormsModule,
    NgForOf,
    ReactiveFormsModule,
    NgIf,
  ],
  templateUrl: './edit-task-component.html',
  styleUrl: './edit-task-component.css',
})
export class EditTaskComponent implements OnInit{
  task$!: Task;
  taskForm: FormGroup;

  statuses = [
    { value: 'OPEN', label: 'Открыта' },
    { value: 'IN_PROGRESS', label: 'В работе' },
    { value: 'DONE', label: 'Выполнена' },
    { value: 'CLOSED', label: 'Завершена' }
  ];


  constructor(private taskService: TaskService,private cd: ChangeDetectorRef, private route: ActivatedRoute, private location: Location,private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      title: ['',[
        Validators.required
      ]],
      status: ['',[
        Validators.required
      ]]
    });
  }

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('taskId');
    console.log(id);
    this.taskService.getTask(id).pipe().subscribe({
      next:(response) => {
        this.task$ = response;
        if (this.task$) {
          this.taskForm.patchValue({
            title: this.task$.title,
            status: this.task$.status
          });
        }
      },
      error:(error: HttpErrorResponse) => {
        if(error.status == 404){
          this.location.go('tasks');
          window.location.reload();
        }
      }
    });
  }

  onSubmit() {
    if (this.taskForm.valid) {
      const updatedTask: Task = {
        ...this.task$,
        ...this.taskForm.value
      };
      this.update(updatedTask);
    }

  }


  protected onCancel() {
    this.location.back();
    this.taskService.clearTask();
  }

  protected update(task: Task) {
    if(task.id == null){
      this.taskService.createTask(task).subscribe(() => this.onCancel());
    }else {
      this.taskService.updateTask(task).subscribe(() => this.onCancel());
    }
  }
    get title() {return this.taskForm.get('title');}
}
