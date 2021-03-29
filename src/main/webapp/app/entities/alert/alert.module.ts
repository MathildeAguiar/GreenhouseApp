import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { AlertComponent } from './list/alert.component';
import { AlertDetailComponent } from './detail/alert-detail.component';
import { AlertUpdateComponent } from './update/alert-update.component';
import { AlertDeleteDialogComponent } from './delete/alert-delete-dialog.component';
import { AlertRoutingModule } from './route/alert-routing.module';

@NgModule({
  imports: [SharedModule, AlertRoutingModule],
  declarations: [AlertComponent, AlertDetailComponent, AlertUpdateComponent, AlertDeleteDialogComponent],
  entryComponents: [AlertDeleteDialogComponent],
})
export class AlertModule {}
