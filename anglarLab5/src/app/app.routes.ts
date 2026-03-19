import { Routes } from '@angular/router';
import {EditTaskComponent} from "./edit-task-component/edit-task-component"
import {TaskListComponent} from "./task-list-component/task-list-component"
import {LoginComponent} from "./login-component/login-component"

export const routes: Routes = [
  {path: 'tasks', component: TaskListComponent},
  {path: 'tasks/:taskId', component: EditTaskComponent},
  {path: 'tasks/new', component: EditTaskComponent},
  {path: '', component: LoginComponent}
];
