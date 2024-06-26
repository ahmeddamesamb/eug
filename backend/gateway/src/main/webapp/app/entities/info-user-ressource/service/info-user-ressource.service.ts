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
import { IInfoUserRessource, NewInfoUserRessource } from '../info-user-ressource.model';

export type PartialUpdateInfoUserRessource = Partial<IInfoUserRessource> & Pick<IInfoUserRessource, 'id'>;

type RestOf<T extends IInfoUserRessource | NewInfoUserRessource> = Omit<T, 'dateAjout'> & {
  dateAjout?: string | null;
};

export type RestInfoUserRessource = RestOf<IInfoUserRessource>;

export type NewRestInfoUserRessource = RestOf<NewInfoUserRessource>;

export type PartialUpdateRestInfoUserRessource = RestOf<PartialUpdateInfoUserRessource>;

export type EntityResponseType = HttpResponse<IInfoUserRessource>;
export type EntityArrayResponseType = HttpResponse<IInfoUserRessource[]>;

@Injectable({ providedIn: 'root' })
export class InfoUserRessourceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/info-user-ressources');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/info-user-ressources/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(infoUserRessource: NewInfoUserRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoUserRessource);
    return this.http
      .post<RestInfoUserRessource>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(infoUserRessource: IInfoUserRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoUserRessource);
    return this.http
      .put<RestInfoUserRessource>(`${this.resourceUrl}/${this.getInfoUserRessourceIdentifier(infoUserRessource)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(infoUserRessource: PartialUpdateInfoUserRessource): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infoUserRessource);
    return this.http
      .patch<RestInfoUserRessource>(`${this.resourceUrl}/${this.getInfoUserRessourceIdentifier(infoUserRessource)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInfoUserRessource>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInfoUserRessource[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestInfoUserRessource[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IInfoUserRessource[]>()], asapScheduler)),
    );
  }

  getInfoUserRessourceIdentifier(infoUserRessource: Pick<IInfoUserRessource, 'id'>): number {
    return infoUserRessource.id;
  }

  compareInfoUserRessource(o1: Pick<IInfoUserRessource, 'id'> | null, o2: Pick<IInfoUserRessource, 'id'> | null): boolean {
    return o1 && o2 ? this.getInfoUserRessourceIdentifier(o1) === this.getInfoUserRessourceIdentifier(o2) : o1 === o2;
  }

  addInfoUserRessourceToCollectionIfMissing<Type extends Pick<IInfoUserRessource, 'id'>>(
    infoUserRessourceCollection: Type[],
    ...infoUserRessourcesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const infoUserRessources: Type[] = infoUserRessourcesToCheck.filter(isPresent);
    if (infoUserRessources.length > 0) {
      const infoUserRessourceCollectionIdentifiers = infoUserRessourceCollection.map(
        infoUserRessourceItem => this.getInfoUserRessourceIdentifier(infoUserRessourceItem)!,
      );
      const infoUserRessourcesToAdd = infoUserRessources.filter(infoUserRessourceItem => {
        const infoUserRessourceIdentifier = this.getInfoUserRessourceIdentifier(infoUserRessourceItem);
        if (infoUserRessourceCollectionIdentifiers.includes(infoUserRessourceIdentifier)) {
          return false;
        }
        infoUserRessourceCollectionIdentifiers.push(infoUserRessourceIdentifier);
        return true;
      });
      return [...infoUserRessourcesToAdd, ...infoUserRessourceCollection];
    }
    return infoUserRessourceCollection;
  }

  protected convertDateFromClient<T extends IInfoUserRessource | NewInfoUserRessource | PartialUpdateInfoUserRessource>(
    infoUserRessource: T,
  ): RestOf<T> {
    return {
      ...infoUserRessource,
      dateAjout: infoUserRessource.dateAjout?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInfoUserRessource: RestInfoUserRessource): IInfoUserRessource {
    return {
      ...restInfoUserRessource,
      dateAjout: restInfoUserRessource.dateAjout ? dayjs(restInfoUserRessource.dateAjout) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInfoUserRessource>): HttpResponse<IInfoUserRessource> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInfoUserRessource[]>): HttpResponse<IInfoUserRessource[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
