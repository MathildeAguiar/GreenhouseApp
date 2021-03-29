jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAlert, Alert } from '../alert.model';
import { AlertService } from '../service/alert.service';

import { AlertRoutingResolveService } from './alert-routing-resolve.service';

describe('Service Tests', () => {
  describe('Alert routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AlertRoutingResolveService;
    let service: AlertService;
    let resultAlert: IAlert | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AlertRoutingResolveService);
      service = TestBed.inject(AlertService);
      resultAlert = undefined;
    });

    describe('resolve', () => {
      it('should return IAlert returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlert = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAlert).toEqual({ id: 123 });
      });

      it('should return new IAlert if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlert = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAlert).toEqual(new Alert());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAlert = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAlert).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
