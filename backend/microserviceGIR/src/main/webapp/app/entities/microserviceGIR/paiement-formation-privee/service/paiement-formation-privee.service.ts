import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IPaiementFormationPrivee, NewPaiementFormationPrivee } from '../paiement-formation-privee.model';

export type PartialUpdatePaiementFormationPrivee = Partial<IPaiementFormationPrivee> & Pick<IPaiementFormationPrivee, 'id'>;

type RestOf<T extends IPaiementFormationPrivee | NewPaiementFormationPrivee> = Omit<T, 'datePaiement'> & {
  datePaiement?: string | null;
};

export type RestPaiementFormationPrivee = RestOf<IPaiementFormationPrivee>;

export type NewRestPaiementFormationPrivee = RestOf<NewPaiementFormationPrivee>;

export type PartialUpdateRestPaiementFormationPrivee = RestOf<PartialUpdatePaiementFormationPrivee>;

export type EntityResponseType = HttpResponse<IPaiementFormationPrivee>;
export type EntityArrayResponseType = HttpResponse<IPaiementFormationPrivee[]>;

@Injectable({ providedIn: 'root' })
export class PaiementFormationPriveeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-formation-privees', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/paiement-formation-privees/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(paiementFormationPrivee: NewPaiementFormationPrivee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFormationPrivee);
    return this.http
      .post<RestPaiementFormationPrivee>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paiementFormationPrivee: IPaiementFormationPrivee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFormationPrivee);
    return this.http
      .put<RestPaiementFormationPrivee>(`${this.resourceUrl}/${this.getPaiementFormationPriveeIdentifier(paiementFormationPrivee)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paiementFormationPrivee: PartialUpdatePaiementFormationPrivee): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFormationPrivee);
    return this.http
      .patch<RestPaiementFormationPrivee>(
        `${this.resourceUrl}/${this.getPaiementFormationPriveeIdentifier(paiementFormationPrivee)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPaiementFormationPrivee>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaiementFormationPrivee[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestPaiementFormationPrivee[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IPaiementFormationPrivee[]>()], asapScheduler)),
    );
  }

  getPaiementFormationPriveeIdentifier(paiementFormationPrivee: Pick<IPaiementFormationPrivee, 'id'>): number {
    return paiementFormationPrivee.id;
  }

  comparePaiementFormationPrivee(
    o1: Pick<IPaiementFormationPrivee, 'id'> | null,
    o2: Pick<IPaiementFormationPrivee, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getPaiementFormationPriveeIdentifier(o1) === this.getPaiementFormationPriveeIdentifier(o2) : o1 === o2;
  }

  addPaiementFormationPriveeToCollectionIfMissing<Type extends Pick<IPaiementFormationPrivee, 'id'>>(
    paiementFormationPriveeCollection: Type[],
    ...paiementFormationPriveesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementFormationPrivees: Type[] = paiementFormationPriveesToCheck.filter(isPresent);
    if (paiementFormationPrivees.length > 0) {
      const paiementFormationPriveeCollectionIdentifiers = paiementFormationPriveeCollection.map(
        paiementFormationPriveeItem => this.getPaiementFormationPriveeIdentifier(paiementFormationPriveeItem)!,
      );
      const paiementFormationPriveesToAdd = paiementFormationPrivees.filter(paiementFormationPriveeItem => {
        const paiementFormationPriveeIdentifier = this.getPaiementFormationPriveeIdentifier(paiementFormationPriveeItem);
        if (paiementFormationPriveeCollectionIdentifiers.includes(paiementFormationPriveeIdentifier)) {
          return false;
        }
        paiementFormationPriveeCollectionIdentifiers.push(paiementFormationPriveeIdentifier);
        return true;
      });
      return [...paiementFormationPriveesToAdd, ...paiementFormationPriveeCollection];
    }
    return paiementFormationPriveeCollection;
  }

  protected convertDateFromClient<T extends IPaiementFormationPrivee | NewPaiementFormationPrivee | PartialUpdatePaiementFormationPrivee>(
    paiementFormationPrivee: T,
  ): RestOf<T> {
    return {
      ...paiementFormationPrivee,
      datePaiement: paiementFormationPrivee.datePaiement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restPaiementFormationPrivee: RestPaiementFormationPrivee): IPaiementFormationPrivee {
    return {
      ...restPaiementFormationPrivee,
      datePaiement: restPaiementFormationPrivee.datePaiement ? dayjs(restPaiementFormationPrivee.datePaiement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPaiementFormationPrivee>): HttpResponse<IPaiementFormationPrivee> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPaiementFormationPrivee[]>): HttpResponse<IPaiementFormationPrivee[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
