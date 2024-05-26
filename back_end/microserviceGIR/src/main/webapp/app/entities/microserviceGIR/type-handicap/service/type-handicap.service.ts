import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeHandicap, NewTypeHandicap } from '../type-handicap.model';

export type PartialUpdateTypeHandicap = Partial<ITypeHandicap> & Pick<ITypeHandicap, 'id'>;

export type EntityResponseType = HttpResponse<ITypeHandicap>;
export type EntityArrayResponseType = HttpResponse<ITypeHandicap[]>;

@Injectable({ providedIn: 'root' })
export class TypeHandicapService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-handicaps', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeHandicap: NewTypeHandicap): Observable<EntityResponseType> {
    return this.http.post<ITypeHandicap>(this.resourceUrl, typeHandicap, { observe: 'response' });
  }

  update(typeHandicap: ITypeHandicap): Observable<EntityResponseType> {
    return this.http.put<ITypeHandicap>(`${this.resourceUrl}/${this.getTypeHandicapIdentifier(typeHandicap)}`, typeHandicap, {
      observe: 'response',
    });
  }

  partialUpdate(typeHandicap: PartialUpdateTypeHandicap): Observable<EntityResponseType> {
    return this.http.patch<ITypeHandicap>(`${this.resourceUrl}/${this.getTypeHandicapIdentifier(typeHandicap)}`, typeHandicap, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeHandicap>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeHandicap[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeHandicapIdentifier(typeHandicap: Pick<ITypeHandicap, 'id'>): number {
    return typeHandicap.id;
  }

  compareTypeHandicap(o1: Pick<ITypeHandicap, 'id'> | null, o2: Pick<ITypeHandicap, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeHandicapIdentifier(o1) === this.getTypeHandicapIdentifier(o2) : o1 === o2;
  }

  addTypeHandicapToCollectionIfMissing<Type extends Pick<ITypeHandicap, 'id'>>(
    typeHandicapCollection: Type[],
    ...typeHandicapsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeHandicaps: Type[] = typeHandicapsToCheck.filter(isPresent);
    if (typeHandicaps.length > 0) {
      const typeHandicapCollectionIdentifiers = typeHandicapCollection.map(
        typeHandicapItem => this.getTypeHandicapIdentifier(typeHandicapItem)!,
      );
      const typeHandicapsToAdd = typeHandicaps.filter(typeHandicapItem => {
        const typeHandicapIdentifier = this.getTypeHandicapIdentifier(typeHandicapItem);
        if (typeHandicapCollectionIdentifiers.includes(typeHandicapIdentifier)) {
          return false;
        }
        typeHandicapCollectionIdentifiers.push(typeHandicapIdentifier);
        return true;
      });
      return [...typeHandicapsToAdd, ...typeHandicapCollection];
    }
    return typeHandicapCollection;
  }
}
