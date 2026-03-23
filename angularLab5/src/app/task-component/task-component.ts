import { Component, Input, Output, EventEmitter } from '@angular/core';
import {RouterLink} from '@angular/router';
import {Task} from '../interfaces/task';
import {DatePipe} from '@angular/common';

@Component({
  selector: '[app-task-component]',
  imports: [
    RouterLink,
    DatePipe
  ],
  templateUrl: './task-component.html',
  styleUrl: './task-component.css',
})
export class TaskComponent {
  @Input() task!: Task;
  @Output() delete = new EventEmitter<number>();

  onDelete(): void {
    this.delete.emit(this.task.id);
  }
}
