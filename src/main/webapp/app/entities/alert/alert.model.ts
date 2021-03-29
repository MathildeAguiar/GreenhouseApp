import * as dayjs from 'dayjs';
import { AlertLevel } from 'app/entities/enumerations/alert-level.model';

export interface IAlert {
  id?: number;
  level?: AlertLevel;
  createdAt?: dayjs.Dayjs;
  modifiedAt?: dayjs.Dayjs;
}

export class Alert implements IAlert {
  constructor(public id?: number, public level?: AlertLevel, public createdAt?: dayjs.Dayjs, public modifiedAt?: dayjs.Dayjs) {}
}

export function getAlertIdentifier(alert: IAlert): number | undefined {
  return alert.id;
}
