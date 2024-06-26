import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPlanning, NewPlanning } from '../planning.model';

export type PartialUpdatePlanning = Partial<IPlanning> & Pick<IPlanning, 'id'>;

type RestOf<T extends IPlanning | NewPlanning> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestPlanning = RestOf<IPlanning>;

export type NewRestPlanning = RestOf<NewPlanning>;

export type PartialUpdateRestPlanning = RestOf<PartialUpdatePlanning>;

export type EntityResponseType = HttpResponse<IPlanning>;
export type EntityArrayResponseType = HttpResponse<IPlanning[]>;

@Injectable({ providedIn: 'root' })
export class PlanningService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/plannings', 'microserviceedt');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/plannings/_search', 'microserviceedt');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(planning: NewPlanning): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planning);
    return this.http
      .post<RestPlanning>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(planning: IPlanning): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planning);
    return this.http
      .put<RestPlanning>(`${this.resourceUrl}/${this.getPlanningIdentifier(planning)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(planning: PartialUpdatePlanning): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(planning);
    return this.http
      .patch<RestPlanning>(`${this.resourceUrl}/${this.getPlanningIdentifier(planning)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPlanning>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPlanning[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestPlanning[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IPlanning[]>()], asapScheduler)),
    );
  }

  getPlanningIdentifier(planning: Pick<IPlanning, 'id'>): number {
    return planning.id;
  }

  comparePlanning(o1: Pick<IPlanning, 'id'> | null, o2: Pick<IPlanning, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlanningIdentifier(o1) === this.getPlanningIdentifier(o2) : o1 === o2;
  }

  addPlanningToCollectionIfMissing<Type extends Pick<IPlanning, 'id'>>(
    planningCollection: Type[],
    ...planningsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const plannings: Type[] = planningsToCheck.filter(isPresent);
    if (plannings.length > 0) {
      const planningCollectionIdentifiers = planningCollection.map(planningItem => this.getPlanningIdentifier(planningItem)!);
      const planningsToAdd = plannings.filter(planningItem => {
        const planningIdentifier = this.getPlanningIdentifier(planningItem);
        if (planningCollectionIdentifiers.includes(planningIdentifier)) {
          return false;
        }
        planningCollectionIdentifiers.push(planningIdentifier);
        return true;
      });
      return [...planningsToAdd, ...planningCollection];
    }
    return planningCollection;
  }

  protected convertDateFromClient<T extends IPlanning | NewPlanning | PartialUpdatePlanning>(planning: T): RestOf<T> {
    return {
      ...planning,
      dateDebut: planning.dateDebut?.toJSON() ?? null,
      dateFin: planning.dateFin?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPlanning: RestPlanning): IPlanning {
    return {
      ...restPlanning,
      dateDebut: restPlanning.dateDebut ? dayjs(restPlanning.dateDebut) : undefined,
      dateFin: restPlanning.dateFin ? dayjs(restPlanning.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPlanning>): HttpResponse<IPlanning> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPlanning[]>): HttpResponse<IPlanning[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
