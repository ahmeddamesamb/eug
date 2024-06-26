import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IRessourceAide, NewRessourceAide } from '../ressource-aide.model';

export type PartialUpdateRessourceAide = Partial<IRessourceAide> & Pick<IRessourceAide, 'id'>;

export type EntityResponseType = HttpResponse<IRessourceAide>;
export type EntityArrayResponseType = HttpResponse<IRessourceAide[]>;

@Injectable({ providedIn: 'root' })
export class RessourceAideService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ressource-aides', 'microserviceaide');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ressource-aides/_search', 'microserviceaide');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ressourceAide: NewRessourceAide): Observable<EntityResponseType> {
    return this.http.post<IRessourceAide>(this.resourceUrl, ressourceAide, { observe: 'response' });
  }

  update(ressourceAide: IRessourceAide): Observable<EntityResponseType> {
    return this.http.put<IRessourceAide>(`${this.resourceUrl}/${this.getRessourceAideIdentifier(ressourceAide)}`, ressourceAide, {
      observe: 'response',
    });
  }

  partialUpdate(ressourceAide: PartialUpdateRessourceAide): Observable<EntityResponseType> {
    return this.http.patch<IRessourceAide>(`${this.resourceUrl}/${this.getRessourceAideIdentifier(ressourceAide)}`, ressourceAide, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRessourceAide>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRessourceAide[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRessourceAide[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IRessourceAide[]>()], asapScheduler)));
  }

  getRessourceAideIdentifier(ressourceAide: Pick<IRessourceAide, 'id'>): number {
    return ressourceAide.id;
  }

  compareRessourceAide(o1: Pick<IRessourceAide, 'id'> | null, o2: Pick<IRessourceAide, 'id'> | null): boolean {
    return o1 && o2 ? this.getRessourceAideIdentifier(o1) === this.getRessourceAideIdentifier(o2) : o1 === o2;
  }

  addRessourceAideToCollectionIfMissing<Type extends Pick<IRessourceAide, 'id'>>(
    ressourceAideCollection: Type[],
    ...ressourceAidesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ressourceAides: Type[] = ressourceAidesToCheck.filter(isPresent);
    if (ressourceAides.length > 0) {
      const ressourceAideCollectionIdentifiers = ressourceAideCollection.map(
        ressourceAideItem => this.getRessourceAideIdentifier(ressourceAideItem)!,
      );
      const ressourceAidesToAdd = ressourceAides.filter(ressourceAideItem => {
        const ressourceAideIdentifier = this.getRessourceAideIdentifier(ressourceAideItem);
        if (ressourceAideCollectionIdentifiers.includes(ressourceAideIdentifier)) {
          return false;
        }
        ressourceAideCollectionIdentifiers.push(ressourceAideIdentifier);
        return true;
      });
      return [...ressourceAidesToAdd, ...ressourceAideCollection];
    }
    return ressourceAideCollection;
  }
}
