import * as dayjs from 'dayjs';
import { ITask } from 'app/entities/task/task.model';
import { IProfile } from 'app/entities/profile/profile.model';
import { IGreenHouse } from 'app/entities/green-house/green-house.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IReport {
  id?: number;
  titleR?: string | null;
  alerts?: string | null;
  descript?: string | null;
  createdAt?: dayjs.Dayjs;
  modifiedAt?: dayjs.Dayjs;
  langue?: Language | null;
  tasks?: ITask[] | null;
  author?: IProfile | null;
  house?: IGreenHouse | null;
}

export class Report implements IReport {
  constructor(
    public id?: number,
    public titleR?: string | null,
    public alerts?: string | null,
    public descript?: string | null,
    public createdAt?: dayjs.Dayjs,
    public modifiedAt?: dayjs.Dayjs,
    public langue?: Language | null,
    public tasks?: ITask[] | null,
    public author?: IProfile | null,
    public house?: IGreenHouse | null
  ) {}
}

export function getReportIdentifier(report: IReport): number | undefined {
  return report.id;
}
