import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPaiementFrais, NewPaiementFrais } from '../paiement-frais.model';

export type PartialUpdatePaiementFrais = Partial<IPaiementFrais> & Pick<IPaiementFrais, 'id'>;

type RestOf<T extends IPaiementFrais | NewPaiementFrais> = Omit<T, 'datePaiement'> & {
  datePaiement?: string | null;
};

export type RestPaiementFrais = RestOf<IPaiementFrais>;

export type NewRestPaiementFrais = RestOf<NewPaiementFrais>;

export type PartialUpdateRestPaiementFrais = RestOf<PartialUpdatePaiementFrais>;

export type EntityResponseType = HttpResponse<IPaiementFrais>;
export type EntityArrayResponseType = HttpResponse<IPaiementFrais[]>;

@Injectable({ providedIn: 'root' })
export class PaiementFraisService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/paiement-frais', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(paiementFrais: NewPaiementFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFrais);
    return this.http
      .post<RestPaiementFrais>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(paiementFrais: IPaiementFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFrais);
    return this.http
      .put<RestPaiementFrais>(`${this.resourceUrl}/${this.getPaiementFraisIdentifier(paiementFrais)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(paiementFrais: PartialUpdatePaiementFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(paiementFrais);
    return this.http
      .patch<RestPaiementFrais>(`${this.resourceUrl}/${this.getPaiementFraisIdentifier(paiementFrais)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestPaiementFrais>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestPaiementFrais[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPaiementFraisIdentifier(paiementFrais: Pick<IPaiementFrais, 'id'>): number {
    return paiementFrais.id;
  }

  comparePaiementFrais(o1: Pick<IPaiementFrais, 'id'> | null, o2: Pick<IPaiementFrais, 'id'> | null): boolean {
    return o1 && o2 ? this.getPaiementFraisIdentifier(o1) === this.getPaiementFraisIdentifier(o2) : o1 === o2;
  }

  addPaiementFraisToCollectionIfMissing<Type extends Pick<IPaiementFrais, 'id'>>(
    paiementFraisCollection: Type[],
    ...paiementFraisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const paiementFrais: Type[] = paiementFraisToCheck.filter(isPresent);
    if (paiementFrais.length > 0) {
      const paiementFraisCollectionIdentifiers = paiementFraisCollection.map(
        paiementFraisItem => this.getPaiementFraisIdentifier(paiementFraisItem)!,
      );
      const paiementFraisToAdd = paiementFrais.filter(paiementFraisItem => {
        const paiementFraisIdentifier = this.getPaiementFraisIdentifier(paiementFraisItem);
        if (paiementFraisCollectionIdentifiers.includes(paiementFraisIdentifier)) {
          return false;
        }
        paiementFraisCollectionIdentifiers.push(paiementFraisIdentifier);
        return true;
      });
      return [...paiementFraisToAdd, ...paiementFraisCollection];
    }
    return paiementFraisCollection;
  }

  protected convertDateFromClient<T extends IPaiementFrais | NewPaiementFrais | PartialUpdatePaiementFrais>(paiementFrais: T): RestOf<T> {
    return {
      ...paiementFrais,
      datePaiement: paiementFrais.datePaiement?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restPaiementFrais: RestPaiementFrais): IPaiementFrais {
    return {
      ...restPaiementFrais,
      datePaiement: restPaiementFrais.datePaiement ? dayjs(restPaiementFrais.datePaiement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestPaiementFrais>): HttpResponse<IPaiementFrais> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestPaiementFrais[]>): HttpResponse<IPaiementFrais[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
