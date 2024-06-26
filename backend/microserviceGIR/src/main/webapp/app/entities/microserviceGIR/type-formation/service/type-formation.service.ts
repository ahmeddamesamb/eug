import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITypeFormation, NewTypeFormation } from '../type-formation.model';

export type PartialUpdateTypeFormation = Partial<ITypeFormation> & Pick<ITypeFormation, 'id'>;

export type EntityResponseType = HttpResponse<ITypeFormation>;
export type EntityArrayResponseType = HttpResponse<ITypeFormation[]>;

@Injectable({ providedIn: 'root' })
export class TypeFormationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-formations', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/type-formations/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeFormation: NewTypeFormation): Observable<EntityResponseType> {
    return this.http.post<ITypeFormation>(this.resourceUrl, typeFormation, { observe: 'response' });
  }

  update(typeFormation: ITypeFormation): Observable<EntityResponseType> {
    return this.http.put<ITypeFormation>(`${this.resourceUrl}/${this.getTypeFormationIdentifier(typeFormation)}`, typeFormation, {
      observe: 'response',
    });
  }

  partialUpdate(typeFormation: PartialUpdateTypeFormation): Observable<EntityResponseType> {
    return this.http.patch<ITypeFormation>(`${this.resourceUrl}/${this.getTypeFormationIdentifier(typeFormation)}`, typeFormation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeFormation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeFormation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeFormation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITypeFormation[]>()], asapScheduler)));
  }

  getTypeFormationIdentifier(typeFormation: Pick<ITypeFormation, 'id'>): number {
    return typeFormation.id;
  }

  compareTypeFormation(o1: Pick<ITypeFormation, 'id'> | null, o2: Pick<ITypeFormation, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeFormationIdentifier(o1) === this.getTypeFormationIdentifier(o2) : o1 === o2;
  }

  addTypeFormationToCollectionIfMissing<Type extends Pick<ITypeFormation, 'id'>>(
    typeFormationCollection: Type[],
    ...typeFormationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeFormations: Type[] = typeFormationsToCheck.filter(isPresent);
    if (typeFormations.length > 0) {
      const typeFormationCollectionIdentifiers = typeFormationCollection.map(
        typeFormationItem => this.getTypeFormationIdentifier(typeFormationItem)!,
      );
      const typeFormationsToAdd = typeFormations.filter(typeFormationItem => {
        const typeFormationIdentifier = this.getTypeFormationIdentifier(typeFormationItem);
        if (typeFormationCollectionIdentifiers.includes(typeFormationIdentifier)) {
          return false;
        }
        typeFormationCollectionIdentifiers.push(typeFormationIdentifier);
        return true;
      });
      return [...typeFormationsToAdd, ...typeFormationCollection];
    }
    return typeFormationCollection;
  }
}
