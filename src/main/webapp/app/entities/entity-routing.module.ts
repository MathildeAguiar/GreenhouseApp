import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'profile',
        data: { pageTitle: 'greenhouseApp.profile.home.title' },
        loadChildren: () => import('./profile/profile.module').then(m => m.ProfileModule),
      },
      {
        path: 'green-house',
        data: { pageTitle: 'greenhouseApp.greenHouse.home.title' },
        loadChildren: () => import('./green-house/green-house.module').then(m => m.GreenHouseModule),
      },
      {
        path: 'task',
        data: { pageTitle: 'greenhouseApp.task.home.title' },
        loadChildren: () => import('./task/task.module').then(m => m.TaskModule),
      },
      {
        path: 'alert',
        data: { pageTitle: 'greenhouseApp.alert.home.title' },
        loadChildren: () => import('./alert/alert.module').then(m => m.AlertModule),
      },
      {
        path: 'report',
        data: { pageTitle: 'greenhouseApp.report.home.title' },
        loadChildren: () => import('./report/report.module').then(m => m.ReportModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
