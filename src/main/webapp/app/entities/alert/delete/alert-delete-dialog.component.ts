import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAlert } from '../alert.model';
import { AlertService } from '../service/alert.service';

@Component({
  templateUrl: './alert-delete-dialog.component.html',
})
export class AlertDeleteDialogComponent {
  alert?: IAlert;

  constructor(protected alertService: AlertService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alertService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
