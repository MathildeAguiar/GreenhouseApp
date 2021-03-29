import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITask, getTaskIdentifier } from '../task.model';

export type EntityResponseType = HttpResponse<ITask>;
export type EntityArrayResponseType = HttpResponse<ITask[]>;

@Injectable({ providedIn: 'root' })
export class TaskService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/tasks');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(task: ITask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .post<ITask>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(task: ITask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .put<ITask>(`${this.resourceUrl}/${getTaskIdentifier(task) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(task: ITask): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(task);
    return this.http
      .patch<ITask>(`${this.resourceUrl}/${getTaskIdentifier(task) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITask>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITask[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTaskToCollectionIfMissing(taskCollection: ITask[], ...tasksToCheck: (ITask | null | undefined)[]): ITask[] {
    const tasks: ITask[] = tasksToCheck.filter(isPresent);
    if (tasks.length > 0) {
      const taskCollectionIdentifiers = taskCollection.map(taskItem => getTaskIdentifier(taskItem)!);
      const tasksToAdd = tasks.filter(taskItem => {
        const taskIdentifier = getTaskIdentifier(taskItem);
        if (taskIdentifier == null || taskCollectionIdentifiers.includes(taskIdentifier)) {
          return false;
        }
        taskCollectionIdentifiers.push(taskIdentifier);
        return true;
      });
      return [...tasksToAdd, ...taskCollection];
    }
    return taskCollection;
  }

  protected convertDateFromClient(task: ITask): ITask {
    return Object.assign({}, task, {
      startTime: task.startTime?.isValid() ? task.startTime.toJSON() : undefined,
      endTime: task.endTime?.isValid() ? task.endTime.toJSON() : undefined,
      createdAt: task.createdAt?.isValid() ? task.createdAt.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startTime = res.body.startTime ? dayjs(res.body.startTime) : undefined;
      res.body.endTime = res.body.endTime ? dayjs(res.body.endTime) : undefined;
      res.body.createdAt = res.body.createdAt ? dayjs(res.body.createdAt) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((task: ITask) => {
        task.startTime = task.startTime ? dayjs(task.startTime) : undefined;
        task.endTime = task.endTime ? dayjs(task.endTime) : undefined;
        task.createdAt = task.createdAt ? dayjs(task.createdAt) : undefined;
      });
    }
    return res;
  }
}
