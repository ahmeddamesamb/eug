import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeSelection, NewTypeSelection } from '../type-selection.model';

export type PartialUpdateTypeSelection = Partial<ITypeSelection> & Pick<ITypeSelection, 'id'>;

export type EntityResponseType = HttpResponse<ITypeSelection>;
export type EntityArrayResponseType = HttpResponse<ITypeSelection[]>;

@Injectable({ providedIn: 'root' })
export class TypeSelectionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-selections', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeSelection: NewTypeSelection): Observable<EntityResponseType> {
    return this.http.post<ITypeSelection>(this.resourceUrl, typeSelection, { observe: 'response' });
  }

  update(typeSelection: ITypeSelection): Observable<EntityResponseType> {
    return this.http.put<ITypeSelection>(`${this.resourceUrl}/${this.getTypeSelectionIdentifier(typeSelection)}`, typeSelection, {
      observe: 'response',
    });
  }

  partialUpdate(typeSelection: PartialUpdateTypeSelection): Observable<EntityResponseType> {
    return this.http.patch<ITypeSelection>(`${this.resourceUrl}/${this.getTypeSelectionIdentifier(typeSelection)}`, typeSelection, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeSelection>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeSelection[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeSelectionIdentifier(typeSelection: Pick<ITypeSelection, 'id'>): number {
    return typeSelection.id;
  }

  compareTypeSelection(o1: Pick<ITypeSelection, 'id'> | null, o2: Pick<ITypeSelection, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeSelectionIdentifier(o1) === this.getTypeSelectionIdentifier(o2) : o1 === o2;
  }

  addTypeSelectionToCollectionIfMissing<Type extends Pick<ITypeSelection, 'id'>>(
    typeSelectionCollection: Type[],
    ...typeSelectionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeSelections: Type[] = typeSelectionsToCheck.filter(isPresent);
    if (typeSelections.length > 0) {
      const typeSelectionCollectionIdentifiers = typeSelectionCollection.map(
        typeSelectionItem => this.getTypeSelectionIdentifier(typeSelectionItem)!,
      );
      const typeSelectionsToAdd = typeSelections.filter(typeSelectionItem => {
        const typeSelectionIdentifier = this.getTypeSelectionIdentifier(typeSelectionItem);
        if (typeSelectionCollectionIdentifiers.includes(typeSelectionIdentifier)) {
          return false;
        }
        typeSelectionCollectionIdentifiers.push(typeSelectionIdentifier);
        return true;
      });
      return [...typeSelectionsToAdd, ...typeSelectionCollection];
    }
    return typeSelectionCollection;
  }
}
