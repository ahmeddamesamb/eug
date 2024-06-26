import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFormationAutorisee, NewFormationAutorisee } from '../formation-autorisee.model';

export type PartialUpdateFormationAutorisee = Partial<IFormationAutorisee> & Pick<IFormationAutorisee, 'id'>;

type RestOf<T extends IFormationAutorisee | NewFormationAutorisee> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestFormationAutorisee = RestOf<IFormationAutorisee>;

export type NewRestFormationAutorisee = RestOf<NewFormationAutorisee>;

export type PartialUpdateRestFormationAutorisee = RestOf<PartialUpdateFormationAutorisee>;

export type EntityResponseType = HttpResponse<IFormationAutorisee>;
export type EntityArrayResponseType = HttpResponse<IFormationAutorisee[]>;

@Injectable({ providedIn: 'root' })
export class FormationAutoriseeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-autorisees', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/formation-autorisees/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formationAutorisee: NewFormationAutorisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationAutorisee);
    return this.http
      .post<RestFormationAutorisee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formationAutorisee: IFormationAutorisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationAutorisee);
    return this.http
      .put<RestFormationAutorisee>(`${this.resourceUrl}/${this.getFormationAutoriseeIdentifier(formationAutorisee)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formationAutorisee: PartialUpdateFormationAutorisee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationAutorisee);
    return this.http
      .patch<RestFormationAutorisee>(`${this.resourceUrl}/${this.getFormationAutoriseeIdentifier(formationAutorisee)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormationAutorisee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormationAutorisee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestFormationAutorisee[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IFormationAutorisee[]>()], asapScheduler)),
    );
  }

  getFormationAutoriseeIdentifier(formationAutorisee: Pick<IFormationAutorisee, 'id'>): number {
    return formationAutorisee.id;
  }

  compareFormationAutorisee(o1: Pick<IFormationAutorisee, 'id'> | null, o2: Pick<IFormationAutorisee, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationAutoriseeIdentifier(o1) === this.getFormationAutoriseeIdentifier(o2) : o1 === o2;
  }

  addFormationAutoriseeToCollectionIfMissing<Type extends Pick<IFormationAutorisee, 'id'>>(
    formationAutoriseeCollection: Type[],
    ...formationAutoriseesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formationAutorisees: Type[] = formationAutoriseesToCheck.filter(isPresent);
    if (formationAutorisees.length > 0) {
      const formationAutoriseeCollectionIdentifiers = formationAutoriseeCollection.map(
        formationAutoriseeItem => this.getFormationAutoriseeIdentifier(formationAutoriseeItem)!,
      );
      const formationAutoriseesToAdd = formationAutorisees.filter(formationAutoriseeItem => {
        const formationAutoriseeIdentifier = this.getFormationAutoriseeIdentifier(formationAutoriseeItem);
        if (formationAutoriseeCollectionIdentifiers.includes(formationAutoriseeIdentifier)) {
          return false;
        }
        formationAutoriseeCollectionIdentifiers.push(formationAutoriseeIdentifier);
        return true;
      });
      return [...formationAutoriseesToAdd, ...formationAutoriseeCollection];
    }
    return formationAutoriseeCollection;
  }

  protected convertDateFromClient<T extends IFormationAutorisee | NewFormationAutorisee | PartialUpdateFormationAutorisee>(
    formationAutorisee: T,
  ): RestOf<T> {
    return {
      ...formationAutorisee,
      dateDebut: formationAutorisee.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: formationAutorisee.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFormationAutorisee: RestFormationAutorisee): IFormationAutorisee {
    return {
      ...restFormationAutorisee,
      dateDebut: restFormationAutorisee.dateDebut ? dayjs(restFormationAutorisee.dateDebut) : undefined,
      dateFin: restFormationAutorisee.dateFin ? dayjs(restFormationAutorisee.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormationAutorisee>): HttpResponse<IFormationAutorisee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormationAutorisee[]>): HttpResponse<IFormationAutorisee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
