jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TaskService } from '../service/task.service';
import { ITask, Task } from '../task.model';
import { IProfile } from 'app/entities/profile/profile.model';
import { ProfileService } from 'app/entities/profile/service/profile.service';
import { IReport } from 'app/entities/report/report.model';
import { ReportService } from 'app/entities/report/service/report.service';

import { TaskUpdateComponent } from './task-update.component';

describe('Component Tests', () => {
  describe('Task Management Update Component', () => {
    let comp: TaskUpdateComponent;
    let fixture: ComponentFixture<TaskUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let taskService: TaskService;
    let profileService: ProfileService;
    let reportService: ReportService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TaskUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TaskUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TaskUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      taskService = TestBed.inject(TaskService);
      profileService = TestBed.inject(ProfileService);
      reportService = TestBed.inject(ReportService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Profile query and add missing value', () => {
        const task: ITask = { id: 456 };
        const responsible: IProfile = { id: 22402 };
        task.responsible = responsible;

        const profileCollection: IProfile[] = [{ id: 34158 }];
        spyOn(profileService, 'query').and.returnValue(of(new HttpResponse({ body: profileCollection })));
        const additionalProfiles = [responsible];
        const expectedCollection: IProfile[] = [...additionalProfiles, ...profileCollection];
        spyOn(profileService, 'addProfileToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(profileService.query).toHaveBeenCalled();
        expect(profileService.addProfileToCollectionIfMissing).toHaveBeenCalledWith(profileCollection, ...additionalProfiles);
        expect(comp.profilesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Report query and add missing value', () => {
        const task: ITask = { id: 456 };
        const rapport: IReport = { id: 98914 };
        task.rapport = rapport;

        const reportCollection: IReport[] = [{ id: 48132 }];
        spyOn(reportService, 'query').and.returnValue(of(new HttpResponse({ body: reportCollection })));
        const additionalReports = [rapport];
        const expectedCollection: IReport[] = [...additionalReports, ...reportCollection];
        spyOn(reportService, 'addReportToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(reportService.query).toHaveBeenCalled();
        expect(reportService.addReportToCollectionIfMissing).toHaveBeenCalledWith(reportCollection, ...additionalReports);
        expect(comp.reportsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const task: ITask = { id: 456 };
        const responsible: IProfile = { id: 97009 };
        task.responsible = responsible;
        const rapport: IReport = { id: 64737 };
        task.rapport = rapport;

        activatedRoute.data = of({ task });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(task));
        expect(comp.profilesSharedCollection).toContain(responsible);
        expect(comp.reportsSharedCollection).toContain(rapport);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const task = { id: 123 };
        spyOn(taskService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(taskService.update).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const task = new Task();
        spyOn(taskService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: task }));
        saveSubject.complete();

        // THEN
        expect(taskService.create).toHaveBeenCalledWith(task);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const task = { id: 123 };
        spyOn(taskService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ task });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(taskService.update).toHaveBeenCalledWith(task);
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

      describe('trackReportById', () => {
        it('Should return tracked Report primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReportById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
