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
import { IInformationPersonnelle, NewInformationPersonnelle } from '../information-personnelle.model';

export type PartialUpdateInformationPersonnelle = Partial<IInformationPersonnelle> & Pick<IInformationPersonnelle, 'id'>;

type RestOf<T extends IInformationPersonnelle | NewInformationPersonnelle> = Omit<T, 'derniereModification'> & {
  derniereModification?: string | null;
};

export type RestInformationPersonnelle = RestOf<IInformationPersonnelle>;

export type NewRestInformationPersonnelle = RestOf<NewInformationPersonnelle>;

export type PartialUpdateRestInformationPersonnelle = RestOf<PartialUpdateInformationPersonnelle>;

export type EntityResponseType = HttpResponse<IInformationPersonnelle>;
export type EntityArrayResponseType = HttpResponse<IInformationPersonnelle[]>;

@Injectable({ providedIn: 'root' })
export class InformationPersonnelleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/information-personnelles', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/information-personnelles/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(informationPersonnelle: NewInformationPersonnelle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(informationPersonnelle);
    return this.http
      .post<RestInformationPersonnelle>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(informationPersonnelle: IInformationPersonnelle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(informationPersonnelle);
    return this.http
      .put<RestInformationPersonnelle>(`${this.resourceUrl}/${this.getInformationPersonnelleIdentifier(informationPersonnelle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(informationPersonnelle: PartialUpdateInformationPersonnelle): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(informationPersonnelle);
    return this.http
      .patch<RestInformationPersonnelle>(`${this.resourceUrl}/${this.getInformationPersonnelleIdentifier(informationPersonnelle)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInformationPersonnelle>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInformationPersonnelle[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestInformationPersonnelle[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IInformationPersonnelle[]>()], asapScheduler)),
    );
  }

  getInformationPersonnelleIdentifier(informationPersonnelle: Pick<IInformationPersonnelle, 'id'>): number {
    return informationPersonnelle.id;
  }

  compareInformationPersonnelle(o1: Pick<IInformationPersonnelle, 'id'> | null, o2: Pick<IInformationPersonnelle, 'id'> | null): boolean {
    return o1 && o2 ? this.getInformationPersonnelleIdentifier(o1) === this.getInformationPersonnelleIdentifier(o2) : o1 === o2;
  }

  addInformationPersonnelleToCollectionIfMissing<Type extends Pick<IInformationPersonnelle, 'id'>>(
    informationPersonnelleCollection: Type[],
    ...informationPersonnellesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const informationPersonnelles: Type[] = informationPersonnellesToCheck.filter(isPresent);
    if (informationPersonnelles.length > 0) {
      const informationPersonnelleCollectionIdentifiers = informationPersonnelleCollection.map(
        informationPersonnelleItem => this.getInformationPersonnelleIdentifier(informationPersonnelleItem)!,
      );
      const informationPersonnellesToAdd = informationPersonnelles.filter(informationPersonnelleItem => {
        const informationPersonnelleIdentifier = this.getInformationPersonnelleIdentifier(informationPersonnelleItem);
        if (informationPersonnelleCollectionIdentifiers.includes(informationPersonnelleIdentifier)) {
          return false;
        }
        informationPersonnelleCollectionIdentifiers.push(informationPersonnelleIdentifier);
        return true;
      });
      return [...informationPersonnellesToAdd, ...informationPersonnelleCollection];
    }
    return informationPersonnelleCollection;
  }

  protected convertDateFromClient<T extends IInformationPersonnelle | NewInformationPersonnelle | PartialUpdateInformationPersonnelle>(
    informationPersonnelle: T,
  ): RestOf<T> {
    return {
      ...informationPersonnelle,
      derniereModification: informationPersonnelle.derniereModification?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInformationPersonnelle: RestInformationPersonnelle): IInformationPersonnelle {
    return {
      ...restInformationPersonnelle,
      derniereModification: restInformationPersonnelle.derniereModification
        ? dayjs(restInformationPersonnelle.derniereModification)
        : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInformationPersonnelle>): HttpResponse<IInformationPersonnelle> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInformationPersonnelle[]>): HttpResponse<IInformationPersonnelle[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
