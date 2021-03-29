import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GreenHouseDetailComponent } from './green-house-detail.component';

describe('Component Tests', () => {
  describe('GreenHouse Management Detail Component', () => {
    let comp: GreenHouseDetailComponent;
    let fixture: ComponentFixture<GreenHouseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [GreenHouseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ greenHouse: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(GreenHouseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GreenHouseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load greenHouse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.greenHouse).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
