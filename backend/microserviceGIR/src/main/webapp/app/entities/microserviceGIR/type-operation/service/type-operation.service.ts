import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITypeOperation, NewTypeOperation } from '../type-operation.model';

export type PartialUpdateTypeOperation = Partial<ITypeOperation> & Pick<ITypeOperation, 'id'>;

export type EntityResponseType = HttpResponse<ITypeOperation>;
export type EntityArrayResponseType = HttpResponse<ITypeOperation[]>;

@Injectable({ providedIn: 'root' })
export class TypeOperationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-operations', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/type-operations/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeOperation: NewTypeOperation): Observable<EntityResponseType> {
    return this.http.post<ITypeOperation>(this.resourceUrl, typeOperation, { observe: 'response' });
  }

  update(typeOperation: ITypeOperation): Observable<EntityResponseType> {
    return this.http.put<ITypeOperation>(`${this.resourceUrl}/${this.getTypeOperationIdentifier(typeOperation)}`, typeOperation, {
      observe: 'response',
    });
  }

  partialUpdate(typeOperation: PartialUpdateTypeOperation): Observable<EntityResponseType> {
    return this.http.patch<ITypeOperation>(`${this.resourceUrl}/${this.getTypeOperationIdentifier(typeOperation)}`, typeOperation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeOperation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeOperation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeOperation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITypeOperation[]>()], asapScheduler)));
  }

  getTypeOperationIdentifier(typeOperation: Pick<ITypeOperation, 'id'>): number {
    return typeOperation.id;
  }

  compareTypeOperation(o1: Pick<ITypeOperation, 'id'> | null, o2: Pick<ITypeOperation, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeOperationIdentifier(o1) === this.getTypeOperationIdentifier(o2) : o1 === o2;
  }

  addTypeOperationToCollectionIfMissing<Type extends Pick<ITypeOperation, 'id'>>(
    typeOperationCollection: Type[],
    ...typeOperationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeOperations: Type[] = typeOperationsToCheck.filter(isPresent);
    if (typeOperations.length > 0) {
      const typeOperationCollectionIdentifiers = typeOperationCollection.map(
        typeOperationItem => this.getTypeOperationIdentifier(typeOperationItem)!,
      );
      const typeOperationsToAdd = typeOperations.filter(typeOperationItem => {
        const typeOperationIdentifier = this.getTypeOperationIdentifier(typeOperationItem);
        if (typeOperationCollectionIdentifiers.includes(typeOperationIdentifier)) {
          return false;
        }
        typeOperationCollectionIdentifiers.push(typeOperationIdentifier);
        return true;
      });
      return [...typeOperationsToAdd, ...typeOperationCollection];
    }
    return typeOperationCollection;
  }
}
