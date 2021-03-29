jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AlertService } from '../service/alert.service';
import { IAlert, Alert } from '../alert.model';

import { AlertUpdateComponent } from './alert-update.component';

describe('Component Tests', () => {
  describe('Alert Management Update Component', () => {
    let comp: AlertUpdateComponent;
    let fixture: ComponentFixture<AlertUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let alertService: AlertService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AlertUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AlertUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AlertUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      alertService = TestBed.inject(AlertService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const alert: IAlert = { id: 456 };

        activatedRoute.data = of({ alert });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(alert));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const alert = { id: 123 };
        spyOn(alertService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ alert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: alert }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(alertService.update).toHaveBeenCalledWith(alert);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const alert = new Alert();
        spyOn(alertService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ alert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: alert }));
        saveSubject.complete();

        // THEN
        expect(alertService.create).toHaveBeenCalledWith(alert);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const alert = { id: 123 };
        spyOn(alertService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ alert });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(alertService.update).toHaveBeenCalledWith(alert);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
