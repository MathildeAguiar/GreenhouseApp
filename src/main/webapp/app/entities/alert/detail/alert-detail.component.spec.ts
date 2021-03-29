import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AlertDetailComponent } from './alert-detail.component';

describe('Component Tests', () => {
  describe('Alert Management Detail Component', () => {
    let comp: AlertDetailComponent;
    let fixture: ComponentFixture<AlertDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AlertDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ alert: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AlertDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AlertDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load alert on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.alert).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
