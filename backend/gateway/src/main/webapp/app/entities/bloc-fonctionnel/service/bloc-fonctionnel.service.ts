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
import { IBlocFonctionnel, NewBlocFonctionnel } from '../bloc-fonctionnel.model';

export type PartialUpdateBlocFonctionnel = Partial<IBlocFonctionnel> & Pick<IBlocFonctionnel, 'id'>;

type RestOf<T extends IBlocFonctionnel | NewBlocFonctionnel> = Omit<T, 'dateAjoutBloc'> & {
  dateAjoutBloc?: string | null;
};

export type RestBlocFonctionnel = RestOf<IBlocFonctionnel>;

export type NewRestBlocFonctionnel = RestOf<NewBlocFonctionnel>;

export type PartialUpdateRestBlocFonctionnel = RestOf<PartialUpdateBlocFonctionnel>;

export type EntityResponseType = HttpResponse<IBlocFonctionnel>;
export type EntityArrayResponseType = HttpResponse<IBlocFonctionnel[]>;

@Injectable({ providedIn: 'root' })
export class BlocFonctionnelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bloc-fonctionnels');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/bloc-fonctionnels/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(blocFonctionnel: NewBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blocFonctionnel);
    return this.http
      .post<RestBlocFonctionnel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(blocFonctionnel: IBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blocFonctionnel);
    return this.http
      .put<RestBlocFonctionnel>(`${this.resourceUrl}/${this.getBlocFonctionnelIdentifier(blocFonctionnel)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(blocFonctionnel: PartialUpdateBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(blocFonctionnel);
    return this.http
      .patch<RestBlocFonctionnel>(`${this.resourceUrl}/${this.getBlocFonctionnelIdentifier(blocFonctionnel)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBlocFonctionnel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBlocFonctionnel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBlocFonctionnel[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IBlocFonctionnel[]>()], asapScheduler)),
    );
  }

  getBlocFonctionnelIdentifier(blocFonctionnel: Pick<IBlocFonctionnel, 'id'>): number {
    return blocFonctionnel.id;
  }

  compareBlocFonctionnel(o1: Pick<IBlocFonctionnel, 'id'> | null, o2: Pick<IBlocFonctionnel, 'id'> | null): boolean {
    return o1 && o2 ? this.getBlocFonctionnelIdentifier(o1) === this.getBlocFonctionnelIdentifier(o2) : o1 === o2;
  }

  addBlocFonctionnelToCollectionIfMissing<Type extends Pick<IBlocFonctionnel, 'id'>>(
    blocFonctionnelCollection: Type[],
    ...blocFonctionnelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const blocFonctionnels: Type[] = blocFonctionnelsToCheck.filter(isPresent);
    if (blocFonctionnels.length > 0) {
      const blocFonctionnelCollectionIdentifiers = blocFonctionnelCollection.map(
        blocFonctionnelItem => this.getBlocFonctionnelIdentifier(blocFonctionnelItem)!,
      );
      const blocFonctionnelsToAdd = blocFonctionnels.filter(blocFonctionnelItem => {
        const blocFonctionnelIdentifier = this.getBlocFonctionnelIdentifier(blocFonctionnelItem);
        if (blocFonctionnelCollectionIdentifiers.includes(blocFonctionnelIdentifier)) {
          return false;
        }
        blocFonctionnelCollectionIdentifiers.push(blocFonctionnelIdentifier);
        return true;
      });
      return [...blocFonctionnelsToAdd, ...blocFonctionnelCollection];
    }
    return blocFonctionnelCollection;
  }

  protected convertDateFromClient<T extends IBlocFonctionnel | NewBlocFonctionnel | PartialUpdateBlocFonctionnel>(
    blocFonctionnel: T,
  ): RestOf<T> {
    return {
      ...blocFonctionnel,
      dateAjoutBloc: blocFonctionnel.dateAjoutBloc?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBlocFonctionnel: RestBlocFonctionnel): IBlocFonctionnel {
    return {
      ...restBlocFonctionnel,
      dateAjoutBloc: restBlocFonctionnel.dateAjoutBloc ? dayjs(restBlocFonctionnel.dateAjoutBloc) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBlocFonctionnel>): HttpResponse<IBlocFonctionnel> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBlocFonctionnel[]>): HttpResponse<IBlocFonctionnel[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
