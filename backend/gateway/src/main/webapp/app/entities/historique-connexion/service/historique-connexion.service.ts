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
import { IHistoriqueConnexion, NewHistoriqueConnexion } from '../historique-connexion.model';

export type PartialUpdateHistoriqueConnexion = Partial<IHistoriqueConnexion> & Pick<IHistoriqueConnexion, 'id'>;

type RestOf<T extends IHistoriqueConnexion | NewHistoriqueConnexion> = Omit<T, 'dateDebutConnexion' | 'dateFinConnexion'> & {
  dateDebutConnexion?: string | null;
  dateFinConnexion?: string | null;
};

export type RestHistoriqueConnexion = RestOf<IHistoriqueConnexion>;

export type NewRestHistoriqueConnexion = RestOf<NewHistoriqueConnexion>;

export type PartialUpdateRestHistoriqueConnexion = RestOf<PartialUpdateHistoriqueConnexion>;

export type EntityResponseType = HttpResponse<IHistoriqueConnexion>;
export type EntityArrayResponseType = HttpResponse<IHistoriqueConnexion[]>;

@Injectable({ providedIn: 'root' })
export class HistoriqueConnexionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/historique-connexions');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/historique-connexions/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(historiqueConnexion: NewHistoriqueConnexion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueConnexion);
    return this.http
      .post<RestHistoriqueConnexion>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(historiqueConnexion: IHistoriqueConnexion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueConnexion);
    return this.http
      .put<RestHistoriqueConnexion>(`${this.resourceUrl}/${this.getHistoriqueConnexionIdentifier(historiqueConnexion)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(historiqueConnexion: PartialUpdateHistoriqueConnexion): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(historiqueConnexion);
    return this.http
      .patch<RestHistoriqueConnexion>(`${this.resourceUrl}/${this.getHistoriqueConnexionIdentifier(historiqueConnexion)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestHistoriqueConnexion>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestHistoriqueConnexion[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestHistoriqueConnexion[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IHistoriqueConnexion[]>()], asapScheduler)),
    );
  }

  getHistoriqueConnexionIdentifier(historiqueConnexion: Pick<IHistoriqueConnexion, 'id'>): number {
    return historiqueConnexion.id;
  }

  compareHistoriqueConnexion(o1: Pick<IHistoriqueConnexion, 'id'> | null, o2: Pick<IHistoriqueConnexion, 'id'> | null): boolean {
    return o1 && o2 ? this.getHistoriqueConnexionIdentifier(o1) === this.getHistoriqueConnexionIdentifier(o2) : o1 === o2;
  }

  addHistoriqueConnexionToCollectionIfMissing<Type extends Pick<IHistoriqueConnexion, 'id'>>(
    historiqueConnexionCollection: Type[],
    ...historiqueConnexionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const historiqueConnexions: Type[] = historiqueConnexionsToCheck.filter(isPresent);
    if (historiqueConnexions.length > 0) {
      const historiqueConnexionCollectionIdentifiers = historiqueConnexionCollection.map(
        historiqueConnexionItem => this.getHistoriqueConnexionIdentifier(historiqueConnexionItem)!,
      );
      const historiqueConnexionsToAdd = historiqueConnexions.filter(historiqueConnexionItem => {
        const historiqueConnexionIdentifier = this.getHistoriqueConnexionIdentifier(historiqueConnexionItem);
        if (historiqueConnexionCollectionIdentifiers.includes(historiqueConnexionIdentifier)) {
          return false;
        }
        historiqueConnexionCollectionIdentifiers.push(historiqueConnexionIdentifier);
        return true;
      });
      return [...historiqueConnexionsToAdd, ...historiqueConnexionCollection];
    }
    return historiqueConnexionCollection;
  }

  protected convertDateFromClient<T extends IHistoriqueConnexion | NewHistoriqueConnexion | PartialUpdateHistoriqueConnexion>(
    historiqueConnexion: T,
  ): RestOf<T> {
    return {
      ...historiqueConnexion,
      dateDebutConnexion: historiqueConnexion.dateDebutConnexion?.format(DATE_FORMAT) ?? null,
      dateFinConnexion: historiqueConnexion.dateFinConnexion?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restHistoriqueConnexion: RestHistoriqueConnexion): IHistoriqueConnexion {
    return {
      ...restHistoriqueConnexion,
      dateDebutConnexion: restHistoriqueConnexion.dateDebutConnexion ? dayjs(restHistoriqueConnexion.dateDebutConnexion) : undefined,
      dateFinConnexion: restHistoriqueConnexion.dateFinConnexion ? dayjs(restHistoriqueConnexion.dateFinConnexion) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestHistoriqueConnexion>): HttpResponse<IHistoriqueConnexion> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestHistoriqueConnexion[]>): HttpResponse<IHistoriqueConnexion[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
