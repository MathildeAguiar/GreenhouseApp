import * as dayjs from 'dayjs';
import { IProfile } from 'app/entities/profile/profile.model';
import { IReport } from 'app/entities/report/report.model';

export interface ITask {
  id?: number;
  titleT?: string;
  description?: string | null;
  startTime?: dayjs.Dayjs;
  endTime?: dayjs.Dayjs | null;
  createdAt?: dayjs.Dayjs;
  responsible?: IProfile | null;
  rapport?: IReport | null;
}

export class Task implements ITask {
  constructor(
    public id?: number,
    public titleT?: string,
    public description?: string | null,
    public startTime?: dayjs.Dayjs,
    public endTime?: dayjs.Dayjs | null,
    public createdAt?: dayjs.Dayjs,
    public responsible?: IProfile | null,
    public rapport?: IReport | null
  ) {}
}

export function getTaskIdentifier(task: ITask): number | undefined {
  return task.id;
}
