import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFrais, NewFrais } from '../frais.model';

export type PartialUpdateFrais = Partial<IFrais> & Pick<IFrais, 'id'>;

type RestOf<T extends IFrais | NewFrais> = Omit<T, 'dateApplication' | 'dateFin'> & {
  dateApplication?: string | null;
  dateFin?: string | null;
};

export type RestFrais = RestOf<IFrais>;

export type NewRestFrais = RestOf<NewFrais>;

export type PartialUpdateRestFrais = RestOf<PartialUpdateFrais>;

export type EntityResponseType = HttpResponse<IFrais>;
export type EntityArrayResponseType = HttpResponse<IFrais[]>;

@Injectable({ providedIn: 'root' })
export class FraisService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/frais', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(frais: NewFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frais);
    return this.http.post<RestFrais>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(frais: IFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frais);
    return this.http
      .put<RestFrais>(`${this.resourceUrl}/${this.getFraisIdentifier(frais)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(frais: PartialUpdateFrais): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(frais);
    return this.http
      .patch<RestFrais>(`${this.resourceUrl}/${this.getFraisIdentifier(frais)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFrais>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFrais[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFraisIdentifier(frais: Pick<IFrais, 'id'>): number {
    return frais.id;
  }

  compareFrais(o1: Pick<IFrais, 'id'> | null, o2: Pick<IFrais, 'id'> | null): boolean {
    return o1 && o2 ? this.getFraisIdentifier(o1) === this.getFraisIdentifier(o2) : o1 === o2;
  }

  addFraisToCollectionIfMissing<Type extends Pick<IFrais, 'id'>>(
    fraisCollection: Type[],
    ...fraisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const frais: Type[] = fraisToCheck.filter(isPresent);
    if (frais.length > 0) {
      const fraisCollectionIdentifiers = fraisCollection.map(fraisItem => this.getFraisIdentifier(fraisItem)!);
      const fraisToAdd = frais.filter(fraisItem => {
        const fraisIdentifier = this.getFraisIdentifier(fraisItem);
        if (fraisCollectionIdentifiers.includes(fraisIdentifier)) {
          return false;
        }
        fraisCollectionIdentifiers.push(fraisIdentifier);
        return true;
      });
      return [...fraisToAdd, ...fraisCollection];
    }
    return fraisCollection;
  }

  protected convertDateFromClient<T extends IFrais | NewFrais | PartialUpdateFrais>(frais: T): RestOf<T> {
    return {
      ...frais,
      dateApplication: frais.dateApplication?.format(DATE_FORMAT) ?? null,
      dateFin: frais.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFrais: RestFrais): IFrais {
    return {
      ...restFrais,
      dateApplication: restFrais.dateApplication ? dayjs(restFrais.dateApplication) : undefined,
      dateFin: restFrais.dateFin ? dayjs(restFrais.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFrais>): HttpResponse<IFrais> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFrais[]>): HttpResponse<IFrais[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
