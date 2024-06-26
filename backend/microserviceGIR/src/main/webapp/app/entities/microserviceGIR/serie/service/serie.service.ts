import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISerie, NewSerie } from '../serie.model';

export type PartialUpdateSerie = Partial<ISerie> & Pick<ISerie, 'id'>;

export type EntityResponseType = HttpResponse<ISerie>;
export type EntityArrayResponseType = HttpResponse<ISerie[]>;

@Injectable({ providedIn: 'root' })
export class SerieService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/series', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/series/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(serie: NewSerie): Observable<EntityResponseType> {
    return this.http.post<ISerie>(this.resourceUrl, serie, { observe: 'response' });
  }

  update(serie: ISerie): Observable<EntityResponseType> {
    return this.http.put<ISerie>(`${this.resourceUrl}/${this.getSerieIdentifier(serie)}`, serie, { observe: 'response' });
  }

  partialUpdate(serie: PartialUpdateSerie): Observable<EntityResponseType> {
    return this.http.patch<ISerie>(`${this.resourceUrl}/${this.getSerieIdentifier(serie)}`, serie, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISerie>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISerie[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISerie[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ISerie[]>()], asapScheduler)));
  }

  getSerieIdentifier(serie: Pick<ISerie, 'id'>): number {
    return serie.id;
  }

  compareSerie(o1: Pick<ISerie, 'id'> | null, o2: Pick<ISerie, 'id'> | null): boolean {
    return o1 && o2 ? this.getSerieIdentifier(o1) === this.getSerieIdentifier(o2) : o1 === o2;
  }

  addSerieToCollectionIfMissing<Type extends Pick<ISerie, 'id'>>(
    serieCollection: Type[],
    ...seriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const series: Type[] = seriesToCheck.filter(isPresent);
    if (series.length > 0) {
      const serieCollectionIdentifiers = serieCollection.map(serieItem => this.getSerieIdentifier(serieItem)!);
      const seriesToAdd = series.filter(serieItem => {
        const serieIdentifier = this.getSerieIdentifier(serieItem);
        if (serieCollectionIdentifiers.includes(serieIdentifier)) {
          return false;
        }
        serieCollectionIdentifiers.push(serieIdentifier);
        return true;
      });
      return [...seriesToAdd, ...serieCollection];
    }
    return serieCollection;
  }
}
