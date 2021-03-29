import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IGreenHouse, GreenHouse } from '../green-house.model';

import { GreenHouseService } from './green-house.service';

describe('Service Tests', () => {
  describe('GreenHouse Service', () => {
    let service: GreenHouseService;
    let httpMock: HttpTestingController;
    let elemDefault: IGreenHouse;
    let expectedResult: IGreenHouse | IGreenHouse[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(GreenHouseService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nameG: 'AAAAAAA',
        latitude: 0,
        longitude: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a GreenHouse', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new GreenHouse()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a GreenHouse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameG: 'BBBBBB',
            latitude: 1,
            longitude: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a GreenHouse', () => {
        const patchObject = Object.assign(
          {
            nameG: 'BBBBBB',
            longitude: 1,
          },
          new GreenHouse()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of GreenHouse', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nameG: 'BBBBBB',
            latitude: 1,
            longitude: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a GreenHouse', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addGreenHouseToCollectionIfMissing', () => {
        it('should add a GreenHouse to an empty array', () => {
          const greenHouse: IGreenHouse = { id: 123 };
          expectedResult = service.addGreenHouseToCollectionIfMissing([], greenHouse);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(greenHouse);
        });

        it('should not add a GreenHouse to an array that contains it', () => {
          const greenHouse: IGreenHouse = { id: 123 };
          const greenHouseCollection: IGreenHouse[] = [
            {
              ...greenHouse,
            },
            { id: 456 },
          ];
          expectedResult = service.addGreenHouseToCollectionIfMissing(greenHouseCollection, greenHouse);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a GreenHouse to an array that doesn't contain it", () => {
          const greenHouse: IGreenHouse = { id: 123 };
          const greenHouseCollection: IGreenHouse[] = [{ id: 456 }];
          expectedResult = service.addGreenHouseToCollectionIfMissing(greenHouseCollection, greenHouse);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(greenHouse);
        });

        it('should add only unique GreenHouse to an array', () => {
          const greenHouseArray: IGreenHouse[] = [{ id: 123 }, { id: 456 }, { id: 72541 }];
          const greenHouseCollection: IGreenHouse[] = [{ id: 123 }];
          expectedResult = service.addGreenHouseToCollectionIfMissing(greenHouseCollection, ...greenHouseArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const greenHouse: IGreenHouse = { id: 123 };
          const greenHouse2: IGreenHouse = { id: 456 };
          expectedResult = service.addGreenHouseToCollectionIfMissing([], greenHouse, greenHouse2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(greenHouse);
          expect(expectedResult).toContain(greenHouse2);
        });

        it('should accept null and undefined values', () => {
          const greenHouse: IGreenHouse = { id: 123 };
          expectedResult = service.addGreenHouseToCollectionIfMissing([], null, greenHouse, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(greenHouse);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
