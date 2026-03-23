import { Routes } from '@angular/router';
import {EditTaskComponent} from "./edit-task-component/edit-task-component"
import {TaskListComponent} from "./task-list-component/task-list-component"
import {LoginComponent} from "./login-component/login-component"
import {RegistrationUserComponent} from './registration-user-component/registration-user-component';

export const routes: Routes = [
  {path: 'tasks', component: TaskListComponent},
  {path: 'tasks/:taskId', component: EditTaskComponent},
  {path: 'tasks/new', component: EditTaskComponent},
  {path: 'registration', component: RegistrationUserComponent},
  {path: '', component: LoginComponent}
];
