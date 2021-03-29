import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { GreenHouseComponent } from './list/green-house.component';
import { GreenHouseDetailComponent } from './detail/green-house-detail.component';
import { GreenHouseUpdateComponent } from './update/green-house-update.component';
import { GreenHouseDeleteDialogComponent } from './delete/green-house-delete-dialog.component';
import { GreenHouseRoutingModule } from './route/green-house-routing.module';

@NgModule({
  imports: [SharedModule, GreenHouseRoutingModule],
  declarations: [GreenHouseComponent, GreenHouseDetailComponent, GreenHouseUpdateComponent, GreenHouseDeleteDialogComponent],
  entryComponents: [GreenHouseDeleteDialogComponent],
})
export class GreenHouseModule {}
