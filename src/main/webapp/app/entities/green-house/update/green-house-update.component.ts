import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IGreenHouse, GreenHouse } from '../green-house.model';
import { GreenHouseService } from '../service/green-house.service';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';

@Component({
  selector: 'jhi-green-house-update',
  templateUrl: './green-house-update.component.html',
})
export class GreenHouseUpdateComponent implements OnInit {
  isSaving = false;

  profilesSharedCollection: IProfile[] = [];

  editForm = this.fb.group({
    id: [],
    nameG: [null, [Validators.required]],
    latitude: [],
    longitude: [],
    observateur: [],
  });

  constructor(
    protected greenHouseService: GreenHouseService,
    protected profileService: ProfileService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ greenHouse }) => {
      this.updateForm(greenHouse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const greenHouse = this.createFromForm();
    if (greenHouse.id !== undefined) {
      this.subscribeToSaveResponse(this.greenHouseService.update(greenHouse));
    } else {
      this.subscribeToSaveResponse(this.greenHouseService.create(greenHouse));
    }
  }

  trackProfileById(index: number, item: IProfile): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGreenHouse>>): void {
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

  protected updateForm(greenHouse: IGreenHouse): void {
    this.editForm.patchValue({
      id: greenHouse.id,
      nameG: greenHouse.nameG,
      latitude: greenHouse.latitude,
      longitude: greenHouse.longitude,
      observateur: greenHouse.observateur,
    });

    this.profilesSharedCollection = this.profileService.addProfileToCollectionIfMissing(
      this.profilesSharedCollection,
      greenHouse.observateur
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profileService
      .query()
      .pipe(map((res: HttpResponse<IProfile[]>) => res.body ?? []))
      .pipe(
        map((profiles: IProfile[]) =>
          this.profileService.addProfileToCollectionIfMissing(profiles, this.editForm.get('observateur')!.value)
        )
      )
      .subscribe((profiles: IProfile[]) => (this.profilesSharedCollection = profiles));
  }

  protected createFromForm(): IGreenHouse {
    return {
      ...new GreenHouse(),
      id: this.editForm.get(['id'])!.value,
      nameG: this.editForm.get(['nameG'])!.value,
      latitude: this.editForm.get(['latitude'])!.value,
      longitude: this.editForm.get(['longitude'])!.value,
      observateur: this.editForm.get(['observateur'])!.value,
    };
  }
}
