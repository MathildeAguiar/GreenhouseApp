jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { GreenHouseService } from '../service/green-house.service';
import { IGreenHouse, GreenHouse } from '../green-house.model';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';

import { GreenHouseUpdateComponent } from './green-house-update.component';

describe('Component Tests', () => {
  describe('GreenHouse Management Update Component', () => {
    let comp: GreenHouseUpdateComponent;
    let fixture: ComponentFixture<GreenHouseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let greenHouseService: GreenHouseService;
    let profileService: ProfileService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GreenHouseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(GreenHouseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GreenHouseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      greenHouseService = TestBed.inject(GreenHouseService);
      profileService = TestBed.inject(ProfileService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Profile query and add missing value', () => {
        const greenHouse: IGreenHouse = { id: 456 };
        const observateur: IProfile = { id: 24365 };
        greenHouse.observateur = observateur;

        const profileCollection: IProfile[] = [{ id: 49109 }];
        spyOn(profileService, 'query').and.returnValue(of(new HttpResponse({ body: profileCollection })));
        const additionalProfiles = [observateur];
        const expectedCollection: IProfile[] = [...additionalProfiles, ...profileCollection];
        spyOn(profileService, 'addProfileToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ greenHouse });
        comp.ngOnInit();

        expect(profileService.query).toHaveBeenCalled();
        expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(profileCollection, ...additionalProfiles);
        expect(comp.profilesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const greenHouse: IGreenHouse = { id: 456 };
        const observateur: IProfile = { id: 36272 };
        greenHouse.observateur = observateur;

        activatedRoute.data = of({ greenHouse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(greenHouse));
        expect(comp.profilesSharedCollection).toContain(observateur);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const greenHouse = { id: 123 };
        spyOn(greenHouseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ greenHouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: greenHouse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(greenHouseService.update).toHaveBeenCalledWith(greenHouse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const greenHouse = new GreenHouse();
        spyOn(greenHouseService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ greenHouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: greenHouse }));
        saveSubject.complete();

        // THEN
        expect(greenHouseService.create).toHaveBeenCalledWith(greenHouse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const greenHouse = { id: 123 };
        spyOn(greenHouseService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ greenHouse });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(greenHouseService.update).toHaveBeenCalledWith(greenHouse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackProfileById', () => {
        it('Should return tracked Profile primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackProfileById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
