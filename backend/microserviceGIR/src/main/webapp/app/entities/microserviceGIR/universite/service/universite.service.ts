import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IUniversite, NewUniversite } from '../universite.model';

export type PartialUpdateUniversite = Partial<IUniversite> & Pick<IUniversite, 'id'>;

export type EntityResponseType = HttpResponse<IUniversite>;
export type EntityArrayResponseType = HttpResponse<IUniversite[]>;

@Injectable({ providedIn: 'root' })
export class UniversiteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/universites', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/universites/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(universite: NewUniversite): Observable<EntityResponseType> {
    return this.http.post<IUniversite>(this.resourceUrl, universite, { observe: 'response' });
  }

  update(universite: IUniversite): Observable<EntityResponseType> {
    return this.http.put<IUniversite>(`${this.resourceUrl}/${this.getUniversiteIdentifier(universite)}`, universite, {
      observe: 'response',
    });
  }

  partialUpdate(universite: PartialUpdateUniversite): Observable<EntityResponseType> {
    return this.http.patch<IUniversite>(`${this.resourceUrl}/${this.getUniversiteIdentifier(universite)}`, universite, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUniversite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUniversite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUniversite[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IUniversite[]>()], asapScheduler)));
  }

  getUniversiteIdentifier(universite: Pick<IUniversite, 'id'>): number {
    return universite.id;
  }

  compareUniversite(o1: Pick<IUniversite, 'id'> | null, o2: Pick<IUniversite, 'id'> | null): boolean {
    return o1 && o2 ? this.getUniversiteIdentifier(o1) === this.getUniversiteIdentifier(o2) : o1 === o2;
  }

  addUniversiteToCollectionIfMissing<Type extends Pick<IUniversite, 'id'>>(
    universiteCollection: Type[],
    ...universitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const universites: Type[] = universitesToCheck.filter(isPresent);
    if (universites.length > 0) {
      const universiteCollectionIdentifiers = universiteCollection.map(universiteItem => this.getUniversiteIdentifier(universiteItem)!);
      const universitesToAdd = universites.filter(universiteItem => {
        const universiteIdentifier = this.getUniversiteIdentifier(universiteItem);
        if (universiteCollectionIdentifiers.includes(universiteIdentifier)) {
          return false;
        }
        universiteCollectionIdentifiers.push(universiteIdentifier);
        return true;
      });
      return [...universitesToAdd, ...universiteCollection];
    }
    return universiteCollection;
  }
}
