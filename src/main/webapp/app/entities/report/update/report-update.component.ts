import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IReport, Report } from '../report.model';
import { ReportService } from '../service/report.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IGreenHouse } from 'app/entities/green-house/green-house.model';
import { GreenHouseService } from 'app/entities/green-house/service/green-house.service';

@Component({
  selector: 'jhi-report-update',
  templateUrl: './report-update.component.html',
})
export class ReportUpdateComponent implements OnInit {
  isSaving = false;

  profilesSharedCollection: IProfile[] = [];
  greenHousesSharedCollection: IGreenHouse[] = [];

  editForm = this.fb.group({
    id: [],
    titleR: [],
    alerts: [],
    descript: [],
    createdAt: [null, [Validators.required]],
    modifiedAt: [null, [Validators.required]],
    langue: [],
    author: [],
    house: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected reportService: ReportService,
    protected profileService: ProfileService,
    protected greenHouseService: GreenHouseService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ report }) => {
      if (report.id === undefined) {
        const today = dayjs().startOf('day');
        report.createdAt = today;
        report.modifiedAt = today;
      }

      this.updateForm(report);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('greenhouseApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const report = this.createFromForm();
    if (report.id !== undefined) {
      this.subscribeToSaveResponse(this.reportService.update(report));
    } else {
      this.subscribeToSaveResponse(this.reportService.create(report));
    }
  }

  trackProfileById(index: number, item: IProfile): number {
    return item.id!;
  }

  trackGreenHouseById(index: number, item: IGreenHouse): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReport>>): void {
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

  protected updateForm(report: IReport): void {
    this.editForm.patchValue({
      id: report.id,
      titleR: report.titleR,
      alerts: report.alerts,
      descript: report.descript,
      createdAt: report.createdAt ? report.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: report.modifiedAt ? report.modifiedAt.format(DATE_TIME_FORMAT) : null,
      langue: report.langue,
      author: report.author,
      house: report.house,
    });

    this.profilesSharedCollection = this.profileService.addProfileToCollectionIfMissing(this.profilesSharedCollection, report.author);
    this.greenHousesSharedCollection = this.greenHouseService.addGreenHouseToCollectionIfMissing(
      this.greenHousesSharedCollection,
      report.house
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query()
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) => this.profileService.addProfileToCollectionIfMissing(profiles, this.editForm.get('author')!.value))
      )
      .subscribe((profiles: IProfile[]) => (this.profilesSharedCollection = profiles));

    this.greenHouseService
      .query()
      .pipe(map((res: HttpResponse<IGreenHouse[]>) => res.body ?? []))
      .pipe(
        map((greenHouses: IGreenHouse[]) =>
          this.greenHouseService.addGreenHouseToCollectionIfMissing(greenHouses, this.editForm.get('house')!.value)
        )
      )
      .subscribe((greenHouses: IGreenHouse[]) => (this.greenHousesSharedCollection = greenHouses));
  }

  protected createFromForm(): IReport {
    return {
      ...new Report(),
      id: this.editForm.get(['id'])!.value,
      titleR: this.editForm.get(['titleR'])!.value,
      alerts: this.editForm.get(['alerts'])!.value,
      descript: this.editForm.get(['descript'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      modifiedAt: this.editForm.get(['modifiedAt'])!.value ? dayjs(this.editForm.get(['modifiedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      langue: this.editForm.get(['langue'])!.value,
      author: this.editForm.get(['author'])!.value,
      house: this.editForm.get(['house'])!.value,
    };
  }
}
