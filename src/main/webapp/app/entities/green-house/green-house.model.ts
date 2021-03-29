import { IReport } from 'app/entities/report/report.model';
import { IProfile } from 'app/entities/profile/profile.model';

export interface IGreenHouse {
  id?: number;
  nameG?: string;
  latitude?: number | null;
  longitude?: number | null;
  reports?: IReport[] | null;
  observateur?: IProfile | null;
}

export class GreenHouse implements IGreenHouse {
  constructor(
    public id?: number,
    public nameG?: string,
    public latitude?: number | null,
    public longitude?: number | null,
    public reports?: IReport[] | null,
    public observateur?: IProfile | null
  ) {}
}

export function getGreenHouseIdentifier(greenHouse: IGreenHouse): number | undefined {
  return greenHouse.id;
}
