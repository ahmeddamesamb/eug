import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMinistere, NewMinistere } from '../ministere.model';

export type PartialUpdateMinistere = Partial<IMinistere> & Pick<IMinistere, 'id'>;

type RestOf<T extends IMinistere | NewMinistere> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestMinistere = RestOf<IMinistere>;

export type NewRestMinistere = RestOf<NewMinistere>;

export type PartialUpdateRestMinistere = RestOf<PartialUpdateMinistere>;

export type EntityResponseType = HttpResponse<IMinistere>;
export type EntityArrayResponseType = HttpResponse<IMinistere[]>;

@Injectable({ providedIn: 'root' })
export class MinistereService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ministeres', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ministere: NewMinistere): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministere);
    return this.http
      .post<RestMinistere>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ministere: IMinistere): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministere);
    return this.http
      .put<RestMinistere>(`${this.resourceUrl}/${this.getMinistereIdentifier(ministere)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ministere: PartialUpdateMinistere): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ministere);
    return this.http
      .patch<RestMinistere>(`${this.resourceUrl}/${this.getMinistereIdentifier(ministere)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestMinistere>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestMinistere[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMinistereIdentifier(ministere: Pick<IMinistere, 'id'>): number {
    return ministere.id;
  }

  compareMinistere(o1: Pick<IMinistere, 'id'> | null, o2: Pick<IMinistere, 'id'> | null): boolean {
    return o1 && o2 ? this.getMinistereIdentifier(o1) === this.getMinistereIdentifier(o2) : o1 === o2;
  }

  addMinistereToCollectionIfMissing<Type extends Pick<IMinistere, 'id'>>(
    ministereCollection: Type[],
    ...ministeresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ministeres: Type[] = ministeresToCheck.filter(isPresent);
    if (ministeres.length > 0) {
      const ministereCollectionIdentifiers = ministereCollection.map(ministereItem => this.getMinistereIdentifier(ministereItem)!);
      const ministeresToAdd = ministeres.filter(ministereItem => {
        const ministereIdentifier = this.getMinistereIdentifier(ministereItem);
        if (ministereCollectionIdentifiers.includes(ministereIdentifier)) {
          return false;
        }
        ministereCollectionIdentifiers.push(ministereIdentifier);
        return true;
      });
      return [...ministeresToAdd, ...ministereCollection];
    }
    return ministereCollection;
  }

  protected convertDateFromClient<T extends IMinistere | NewMinistere | PartialUpdateMinistere>(ministere: T): RestOf<T> {
    return {
      ...ministere,
      dateDebut: ministere.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: ministere.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restMinistere: RestMinistere): IMinistere {
    return {
      ...restMinistere,
      dateDebut: restMinistere.dateDebut ? dayjs(restMinistere.dateDebut) : undefined,
      dateFin: restMinistere.dateFin ? dayjs(restMinistere.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestMinistere>): HttpResponse<IMinistere> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestMinistere[]>): HttpResponse<IMinistere[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
