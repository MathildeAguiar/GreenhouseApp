import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlert, Alert } from '../alert.model';
import { AlertService } from '../service/alert.service';

@Injectable({ providedIn: 'root' })
export class AlertRoutingResolveService implements Resolve<IAlert> {
  constructor(protected service: AlertService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAlert> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((alert: HttpResponse<Alert>) => {
          if (alert.body) {
            return of(alert.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Alert());
  }
}
