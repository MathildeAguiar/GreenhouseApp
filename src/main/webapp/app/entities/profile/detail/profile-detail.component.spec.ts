import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ProfileDetailComponent } from './profile-detail.component';

describe('Component Tests', () => {
  describe('Profile Management Detail Component', () => {
    let comp: ProfileDetailComponent;
    let fixture: ComponentFixture<ProfileDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ProfileDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ profile: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ProfileDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProfileDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load profile on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.profile).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
