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
import { IBaccalaureat, NewBaccalaureat } from '../baccalaureat.model';

export type PartialUpdateBaccalaureat = Partial<IBaccalaureat> & Pick<IBaccalaureat, 'id'>;

type RestOf<T extends IBaccalaureat | NewBaccalaureat> = Omit<T, 'anneeBac'> & {
  anneeBac?: string | null;
};

export type RestBaccalaureat = RestOf<IBaccalaureat>;

export type NewRestBaccalaureat = RestOf<NewBaccalaureat>;

export type PartialUpdateRestBaccalaureat = RestOf<PartialUpdateBaccalaureat>;

export type EntityResponseType = HttpResponse<IBaccalaureat>;
export type EntityArrayResponseType = HttpResponse<IBaccalaureat[]>;

@Injectable({ providedIn: 'root' })
export class BaccalaureatService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/baccalaureats', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/baccalaureats/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(baccalaureat: NewBaccalaureat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baccalaureat);
    return this.http
      .post<RestBaccalaureat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(baccalaureat: IBaccalaureat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baccalaureat);
    return this.http
      .put<RestBaccalaureat>(`${this.resourceUrl}/${this.getBaccalaureatIdentifier(baccalaureat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(baccalaureat: PartialUpdateBaccalaureat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(baccalaureat);
    return this.http
      .patch<RestBaccalaureat>(`${this.resourceUrl}/${this.getBaccalaureatIdentifier(baccalaureat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBaccalaureat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBaccalaureat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestBaccalaureat[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IBaccalaureat[]>()], asapScheduler)),
    );
  }

  getBaccalaureatIdentifier(baccalaureat: Pick<IBaccalaureat, 'id'>): number {
    return baccalaureat.id;
  }

  compareBaccalaureat(o1: Pick<IBaccalaureat, 'id'> | null, o2: Pick<IBaccalaureat, 'id'> | null): boolean {
    return o1 && o2 ? this.getBaccalaureatIdentifier(o1) === this.getBaccalaureatIdentifier(o2) : o1 === o2;
  }

  addBaccalaureatToCollectionIfMissing<Type extends Pick<IBaccalaureat, 'id'>>(
    baccalaureatCollection: Type[],
    ...baccalaureatsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const baccalaureats: Type[] = baccalaureatsToCheck.filter(isPresent);
    if (baccalaureats.length > 0) {
      const baccalaureatCollectionIdentifiers = baccalaureatCollection.map(
        baccalaureatItem => this.getBaccalaureatIdentifier(baccalaureatItem)!,
      );
      const baccalaureatsToAdd = baccalaureats.filter(baccalaureatItem => {
        const baccalaureatIdentifier = this.getBaccalaureatIdentifier(baccalaureatItem);
        if (baccalaureatCollectionIdentifiers.includes(baccalaureatIdentifier)) {
          return false;
        }
        baccalaureatCollectionIdentifiers.push(baccalaureatIdentifier);
        return true;
      });
      return [...baccalaureatsToAdd, ...baccalaureatCollection];
    }
    return baccalaureatCollection;
  }

  protected convertDateFromClient<T extends IBaccalaureat | NewBaccalaureat | PartialUpdateBaccalaureat>(baccalaureat: T): RestOf<T> {
    return {
      ...baccalaureat,
      anneeBac: baccalaureat.anneeBac?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBaccalaureat: RestBaccalaureat): IBaccalaureat {
    return {
      ...restBaccalaureat,
      anneeBac: restBaccalaureat.anneeBac ? dayjs(restBaccalaureat.anneeBac) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBaccalaureat>): HttpResponse<IBaccalaureat> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBaccalaureat[]>): HttpResponse<IBaccalaureat[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
