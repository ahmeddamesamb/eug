import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ITypeAdmission, NewTypeAdmission } from '../type-admission.model';

export type PartialUpdateTypeAdmission = Partial<ITypeAdmission> & Pick<ITypeAdmission, 'id'>;

export type EntityResponseType = HttpResponse<ITypeAdmission>;
export type EntityArrayResponseType = HttpResponse<ITypeAdmission[]>;

@Injectable({ providedIn: 'root' })
export class TypeAdmissionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-admissions', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/type-admissions/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeAdmission: NewTypeAdmission): Observable<EntityResponseType> {
    return this.http.post<ITypeAdmission>(this.resourceUrl, typeAdmission, { observe: 'response' });
  }

  update(typeAdmission: ITypeAdmission): Observable<EntityResponseType> {
    return this.http.put<ITypeAdmission>(`${this.resourceUrl}/${this.getTypeAdmissionIdentifier(typeAdmission)}`, typeAdmission, {
      observe: 'response',
    });
  }

  partialUpdate(typeAdmission: PartialUpdateTypeAdmission): Observable<EntityResponseType> {
    return this.http.patch<ITypeAdmission>(`${this.resourceUrl}/${this.getTypeAdmissionIdentifier(typeAdmission)}`, typeAdmission, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeAdmission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeAdmission[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITypeAdmission[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ITypeAdmission[]>()], asapScheduler)));
  }

  getTypeAdmissionIdentifier(typeAdmission: Pick<ITypeAdmission, 'id'>): number {
    return typeAdmission.id;
  }

  compareTypeAdmission(o1: Pick<ITypeAdmission, 'id'> | null, o2: Pick<ITypeAdmission, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeAdmissionIdentifier(o1) === this.getTypeAdmissionIdentifier(o2) : o1 === o2;
  }

  addTypeAdmissionToCollectionIfMissing<Type extends Pick<ITypeAdmission, 'id'>>(
    typeAdmissionCollection: Type[],
    ...typeAdmissionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeAdmissions: Type[] = typeAdmissionsToCheck.filter(isPresent);
    if (typeAdmissions.length > 0) {
      const typeAdmissionCollectionIdentifiers = typeAdmissionCollection.map(
        typeAdmissionItem => this.getTypeAdmissionIdentifier(typeAdmissionItem)!,
      );
      const typeAdmissionsToAdd = typeAdmissions.filter(typeAdmissionItem => {
        const typeAdmissionIdentifier = this.getTypeAdmissionIdentifier(typeAdmissionItem);
        if (typeAdmissionCollectionIdentifiers.includes(typeAdmissionIdentifier)) {
          return false;
        }
        typeAdmissionCollectionIdentifiers.push(typeAdmissionIdentifier);
        return true;
      });
      return [...typeAdmissionsToAdd, ...typeAdmissionCollection];
    }
    return typeAdmissionCollection;
  }
}
