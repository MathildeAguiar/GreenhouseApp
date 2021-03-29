import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ITask, Task } from '../task.model';
import { TaskService } from '../service/task.service';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';

@Component({
  selector: 'jhi-task-update',
  templateUrl: './task-update.component.html',
})
export class TaskUpdateComponent implements OnInit {
  isSaving = false;

  profilesSharedCollection: IProfile[] = [];
  reportsSharedCollection: IReport[] = [];

  editForm = this.fb.group({
    id: [],
    titleT: [null, [Validators.required]],
    description: [null, [Validators.minLength(8)]],
    startTime: [null, [Validators.required]],
    endTime: [],
    createdAt: [null, [Validators.required]],
    responsible: [],
    rapport: [],
  });

  constructor(
    protected taskService: TaskService,
    protected profileService: ProfileService,
    protected reportService: ReportService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ task }) => {
      if (task.id === undefined) {
        const today = dayjs().startOf('day');
        task.startTime = today;
        task.endTime = today;
        task.createdAt = today;
      }

      this.updateForm(task);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const task = this.createFromForm();
    if (task.id !== undefined) {
      this.subscribeToSaveResponse(this.taskService.update(task));
    } else {
      this.subscribeToSaveResponse(this.taskService.create(task));
    }
  }

  trackProfileById(index: number, item: IProfile): number {
    return item.id!;
  }

  trackReportById(index: number, item: IReport): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITask>>): void {
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

  protected updateForm(task: ITask): void {
    this.editForm.patchValue({
      id: task.id,
      titleT: task.titleT,
      description: task.description,
      startTime: task.startTime ? task.startTime.format(DATE_TIME_FORMAT) : null,
      endTime: task.endTime ? task.endTime.format(DATE_TIME_FORMAT) : null,
      createdAt: task.createdAt ? task.createdAt.format(DATE_TIME_FORMAT) : null,
      responsible: task.responsible,
      rapport: task.rapport,
    });

    this.profilesSharedCollection = this.profileService.addProfileToCollectionIfMissing(this.profilesSharedCollection, task.responsible);
    this.reportsSharedCollection = this.reportService.addReportToCollectionIfMissing(this.reportsSharedCollection, task.rapport);
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query()
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) =>
          this.profileService.addProfileToCollectionIfMissing(profiles, this.editForm.get('responsible')!.value)
        )
      )
      .subscribe((profiles: IProfile[]) => (this.profilesSharedCollection = profiles));

    this.reportService
      .query()
      .pipe(map((res: HttpResponse<IReport[]>) => res.body ?? []))
      .pipe(map((reports: IReport[]) => this.reportService.addReportToCollectionIfMissing(reports, this.editForm.get('rapport')!.value)))
      .subscribe((reports: IReport[]) => (this.reportsSharedCollection = reports));
  }

  protected createFromForm(): ITask {
    return {
      ...new Task(),
      id: this.editForm.get(['id'])!.value,
      titleT: this.editForm.get(['titleT'])!.value,
      description: this.editForm.get(['description'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      endTime: this.editForm.get(['endTime'])!.value ? dayjs(this.editForm.get(['endTime'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      responsible: this.editForm.get(['responsible'])!.value,
      rapport: this.editForm.get(['rapport'])!.value,
    };
  }
}
