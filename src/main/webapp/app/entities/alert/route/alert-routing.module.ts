import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AlertComponent } from '../list/alert.component';
import { AlertDetailComponent } from '../detail/alert-detail.component';
import { AlertUpdateComponent } from '../update/alert-update.component';
import { AlertRoutingResolveService } from './alert-routing-resolve.service';

const alertRoute: Routes = [
  {
    path: '',
    component: AlertComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlertDetailComponent,
    resolve: {
      alert: AlertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlertUpdateComponent,
    resolve: {
      alert: AlertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlertUpdateComponent,
    resolve: {
      alert: AlertRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(alertRoute)],
  exports: [RouterModule],
})
export class AlertRoutingModule {}
