import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGreenHouse } from '../green-house.model';
import { GreenHouseService } from '../service/green-house.service';

@Component({
  templateUrl: './green-house-delete-dialog.component.html',
})
export class GreenHouseDeleteDialogComponent {
  greenHouse?: IGreenHouse;

  constructor(protected greenHouseService: GreenHouseService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.greenHouseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
