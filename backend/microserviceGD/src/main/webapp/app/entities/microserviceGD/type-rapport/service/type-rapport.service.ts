import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeRapport, NewTypeRapport } from '../type-rapport.model';

export type PartialUpdateTypeRapport = Partial<ITypeRapport> & Pick<ITypeRapport, 'id'>;

export type EntityResponseType = HttpResponse<ITypeRapport>;
export type EntityArrayResponseType = HttpResponse<ITypeRapport[]>;

@Injectable({ providedIn: 'root' })
export class TypeRapportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-rapports', 'microservicegd');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeRapport: NewTypeRapport): Observable<EntityResponseType> {
    return this.http.post<ITypeRapport>(this.resourceUrl, typeRapport, { observe: 'response' });
  }

  update(typeRapport: ITypeRapport): Observable<EntityResponseType> {
    return this.http.put<ITypeRapport>(`${this.resourceUrl}/${this.getTypeRapportIdentifier(typeRapport)}`, typeRapport, {
      observe: 'response',
    });
  }

  partialUpdate(typeRapport: PartialUpdateTypeRapport): Observable<EntityResponseType> {
    return this.http.patch<ITypeRapport>(`${this.resourceUrl}/${this.getTypeRapportIdentifier(typeRapport)}`, typeRapport, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeRapport>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeRapport[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeRapportIdentifier(typeRapport: Pick<ITypeRapport, 'id'>): number {
    return typeRapport.id;
  }

  compareTypeRapport(o1: Pick<ITypeRapport, 'id'> | null, o2: Pick<ITypeRapport, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeRapportIdentifier(o1) === this.getTypeRapportIdentifier(o2) : o1 === o2;
  }

  addTypeRapportToCollectionIfMissing<Type extends Pick<ITypeRapport, 'id'>>(
    typeRapportCollection: Type[],
    ...typeRapportsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeRapports: Type[] = typeRapportsToCheck.filter(isPresent);
    if (typeRapports.length > 0) {
      const typeRapportCollectionIdentifiers = typeRapportCollection.map(
        typeRapportItem => this.getTypeRapportIdentifier(typeRapportItem)!,
      );
      const typeRapportsToAdd = typeRapports.filter(typeRapportItem => {
        const typeRapportIdentifier = this.getTypeRapportIdentifier(typeRapportItem);
        if (typeRapportCollectionIdentifiers.includes(typeRapportIdentifier)) {
          return false;
        }
        typeRapportCollectionIdentifiers.push(typeRapportIdentifier);
        return true;
      });
      return [...typeRapportsToAdd, ...typeRapportCollection];
    }
    return typeRapportCollection;
  }
}
