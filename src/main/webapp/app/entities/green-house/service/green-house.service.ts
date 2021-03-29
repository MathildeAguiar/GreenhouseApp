import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGreenHouse, getGreenHouseIdentifier } from '../green-house.model';

export type EntityResponseType = HttpResponse<IGreenHouse>;
export type EntityArrayResponseType = HttpResponse<IGreenHouse[]>;

@Injectable({ providedIn: 'root' })
export class GreenHouseService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/green-houses');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(greenHouse: IGreenHouse): Observable<EntityResponseType> {
    return this.http.post<IGreenHouse>(this.resourceUrl, greenHouse, { observe: 'response' });
  }

  update(greenHouse: IGreenHouse): Observable<EntityResponseType> {
    return this.http.put<IGreenHouse>(`${this.resourceUrl}/${getGreenHouseIdentifier(greenHouse) as number}`, greenHouse, {
      observe: 'response',
    });
  }

  partialUpdate(greenHouse: IGreenHouse): Observable<EntityResponseType> {
    return this.http.patch<IGreenHouse>(`${this.resourceUrl}/${getGreenHouseIdentifier(greenHouse) as number}`, greenHouse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGreenHouse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGreenHouse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGreenHouseToCollectionIfMissing(
    greenHouseCollection: IGreenHouse[],
    ...greenHousesToCheck: (IGreenHouse | null | undefined)[]
  ): IGreenHouse[] {
    const greenHouses: IGreenHouse[] = greenHousesToCheck.filter(isPresent);
    if (greenHouses.length > 0) {
      const greenHouseCollectionIdentifiers = greenHouseCollection.map(greenHouseItem => getGreenHouseIdentifier(greenHouseItem)!);
      const greenHousesToAdd = greenHouses.filter(greenHouseItem => {
        const greenHouseIdentifier = getGreenHouseIdentifier(greenHouseItem);
        if (greenHouseIdentifier == null || greenHouseCollectionIdentifiers.includes(greenHouseIdentifier)) {
          return false;
        }
        greenHouseCollectionIdentifiers.push(greenHouseIdentifier);
        return true;
      });
      return [...greenHousesToAdd, ...greenHouseCollection];
    }
    return greenHouseCollection;
  }
}
