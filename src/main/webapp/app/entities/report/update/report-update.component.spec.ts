jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReportService } from '../service/report.service';
import { IReport, Report } from '../report.model';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IGreenHouse } from 'app/entities/green-house/green-house.model';
import { GreenHouseService } from 'app/entities/green-house/service/green-house.service';

import { ReportUpdateComponent } from './report-update.component';

describe('Component Tests', () => {
  describe('Report Management Update Component', () => {
    let comp: ReportUpdateComponent;
    let fixture: ComponentFixture<ReportUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reportService: ReportService;
    let profileService: ProfileService;
    let greenHouseService: GreenHouseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReportUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReportUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReportUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reportService = TestBed.inject(ReportService);
      profileService = TestBed.inject(ProfileService);
      greenHouseService = TestBed.inject(GreenHouseService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Profile query and add missing value', () => {
        const report: IReport = { id: 456 };
        const author: IProfile = { id: 70268 };
        report.author = author;

        const profileCollection: IProfile[] = [{ id: 53217 }];
        spyOn(profileService, 'query').and.returnValue(of(new HttpResponse({ body: profileCollection })));
        const additionalProfiles = [author];
        const expectedCollection: IProfile[] = [...additionalProfiles, ...profileCollection];
        spyOn(profileService, 'addProfileToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(profileService.query).toHaveBeenCalled();
        expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(profileCollection, ...additionalProfiles);
        expect(comp.profilesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call GreenHouse query and add missing value', () => {
        const report: IReport = { id: 456 };
        const house: IGreenHouse = { id: 81829 };
        report.house = house;

        const greenHouseCollection: IGreenHouse[] = [{ id: 87339 }];
        spyOn(greenHouseService, 'query').and.returnValue(of(new HttpResponse({ body: greenHouseCollection })));
        const additionalGreenHouses = [house];
        const expectedCollection: IGreenHouse[] = [...additionalGreenHouses, ...greenHouseCollection];
        spyOn(greenHouseService, 'addGreenHouseToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(greenHouseService.query).toHaveBeenCalled();
        expect(greenHouseService.addGreenHouseToCollectionIfMissing).toHaveBeenCalledWith(greenHouseCollection, ...additionalGreenHouses);
        expect(comp.greenHousesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const report: IReport = { id: 456 };
        const author: IProfile = { id: 48908 };
        report.author = author;
        const house: IGreenHouse = { id: 88655 };
        report.house = house;

        activatedRoute.data = of({ report });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(report));
        expect(comp.profilesSharedCollection).toContain(author);
        expect(comp.greenHousesSharedCollection).toContain(house);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = { id: 123 };
        spyOn(reportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: report }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reportService.update).toHaveBeenCalledWith(report);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = new Report();
        spyOn(reportService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: report }));
        saveSubject.complete();

        // THEN
        expect(reportService.create).toHaveBeenCalledWith(report);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const report = { id: 123 };
        spyOn(reportService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ report });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reportService.update).toHaveBeenCalledWith(report);
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

      describe('trackGreenHouseById', () => {
        it('Should return tracked GreenHouse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackGreenHouseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
