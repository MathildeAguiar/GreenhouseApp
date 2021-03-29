jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IGreenHouse, GreenHouse } from '../green-house.model';
import { GreenHouseService } from '../service/green-house.service';

import { GreenHouseRoutingResolveService } from './green-house-routing-resolve.service';

describe('Service Tests', () => {
  describe('GreenHouse routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: GreenHouseRoutingResolveService;
    let service: GreenHouseService;
    let resultGreenHouse: IGreenHouse | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(GreenHouseRoutingResolveService);
      service = TestBed.inject(GreenHouseService);
      resultGreenHouse = undefined;
    });

    describe('resolve', () => {
      it('should return IGreenHouse returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGreenHouse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGreenHouse).toEqual({ id: 123 });
      });

      it('should return new IGreenHouse if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGreenHouse = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultGreenHouse).toEqual(new GreenHouse());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultGreenHouse = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultGreenHouse).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
