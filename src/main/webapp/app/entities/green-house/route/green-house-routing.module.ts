import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GreenHouseComponent } from '../list/green-house.component';
import { GreenHouseDetailComponent } from '../detail/green-house-detail.component';
import { GreenHouseUpdateComponent } from '../update/green-house-update.component';
import { GreenHouseRoutingResolveService } from './green-house-routing-resolve.service';

const greenHouseRoute: Routes = [
  {
    path: '',
    component: GreenHouseComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GreenHouseDetailComponent,
    resolve: {
      greenHouse: GreenHouseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GreenHouseUpdateComponent,
    resolve: {
      greenHouse: GreenHouseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GreenHouseUpdateComponent,
    resolve: {
      greenHouse: GreenHouseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(greenHouseRoute)],
  exports: [RouterModule],
})
export class GreenHouseRoutingModule {}
