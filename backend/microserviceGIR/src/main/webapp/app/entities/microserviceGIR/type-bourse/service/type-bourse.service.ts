import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeBourse, NewTypeBourse } from '../type-bourse.model';

export type PartialUpdateTypeBourse = Partial<ITypeBourse> & Pick<ITypeBourse, 'id'>;

export type EntityResponseType = HttpResponse<ITypeBourse>;
export type EntityArrayResponseType = HttpResponse<ITypeBourse[]>;

@Injectable({ providedIn: 'root' })
export class TypeBourseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-bourses', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeBourse: NewTypeBourse): Observable<EntityResponseType> {
    return this.http.post<ITypeBourse>(this.resourceUrl, typeBourse, { observe: 'response' });
  }

  update(typeBourse: ITypeBourse): Observable<EntityResponseType> {
    return this.http.put<ITypeBourse>(`${this.resourceUrl}/${this.getTypeBourseIdentifier(typeBourse)}`, typeBourse, {
      observe: 'response',
    });
  }

  partialUpdate(typeBourse: PartialUpdateTypeBourse): Observable<EntityResponseType> {
    return this.http.patch<ITypeBourse>(`${this.resourceUrl}/${this.getTypeBourseIdentifier(typeBourse)}`, typeBourse, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeBourse>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeBourse[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeBourseIdentifier(typeBourse: Pick<ITypeBourse, 'id'>): number {
    return typeBourse.id;
  }

  compareTypeBourse(o1: Pick<ITypeBourse, 'id'> | null, o2: Pick<ITypeBourse, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeBourseIdentifier(o1) === this.getTypeBourseIdentifier(o2) : o1 === o2;
  }

  addTypeBourseToCollectionIfMissing<Type extends Pick<ITypeBourse, 'id'>>(
    typeBourseCollection: Type[],
    ...typeBoursesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeBourses: Type[] = typeBoursesToCheck.filter(isPresent);
    if (typeBourses.length > 0) {
      const typeBourseCollectionIdentifiers = typeBourseCollection.map(typeBourseItem => this.getTypeBourseIdentifier(typeBourseItem)!);
      const typeBoursesToAdd = typeBourses.filter(typeBourseItem => {
        const typeBourseIdentifier = this.getTypeBourseIdentifier(typeBourseItem);
        if (typeBourseCollectionIdentifiers.includes(typeBourseIdentifier)) {
          return false;
        }
        typeBourseCollectionIdentifiers.push(typeBourseIdentifier);
        return true;
      });
      return [...typeBoursesToAdd, ...typeBourseCollection];
    }
    return typeBourseCollection;
  }
}
