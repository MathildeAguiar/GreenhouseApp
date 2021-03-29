import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGreenHouse, GreenHouse } from '../green-house.model';
import { GreenHouseService } from '../service/green-house.service';

@Injectable({ providedIn: 'root' })
export class GreenHouseRoutingResolveService implements Resolve<IGreenHouse> {
  constructor(protected service: GreenHouseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGreenHouse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((greenHouse: HttpResponse<GreenHouse>) => {
          if (greenHouse.body) {
            return of(greenHouse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GreenHouse());
  }
}
