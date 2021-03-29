import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAlert, Alert } from '../alert.model';
import { AlertService } from '../service/alert.service';

@Component({
  selector: 'jhi-alert-update',
  templateUrl: './alert-update.component.html',
})
export class AlertUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    level: [null, [Validators.required]],
    createdAt: [null, [Validators.required]],
    modifiedAt: [null, [Validators.required]],
  });

  constructor(protected alertService: AlertService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alert }) => {
      if (alert.id === undefined) {
        const today = dayjs().startOf('day');
        alert.createdAt = today;
        alert.modifiedAt = today;
      }

      this.updateForm(alert);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alert = this.createFromForm();
    if (alert.id !== undefined) {
      this.subscribeToSaveResponse(this.alertService.update(alert));
    } else {
      this.subscribeToSaveResponse(this.alertService.create(alert));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlert>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(alert: IAlert): void {
    this.editForm.patchValue({
      id: alert.id,
      level: alert.level,
      createdAt: alert.createdAt ? alert.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: alert.modifiedAt ? alert.modifiedAt.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IAlert {
    return {
      ...new Alert(),
      id: this.editForm.get(['id'])!.value,
      level: this.editForm.get(['level'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      modifiedAt: this.editForm.get(['modifiedAt'])!.value ? dayjs(this.editForm.get(['modifiedAt'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
