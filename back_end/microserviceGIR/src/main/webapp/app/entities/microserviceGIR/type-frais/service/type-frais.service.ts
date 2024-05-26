import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeFrais, NewTypeFrais } from '../type-frais.model';

export type PartialUpdateTypeFrais = Partial<ITypeFrais> & Pick<ITypeFrais, 'id'>;

export type EntityResponseType = HttpResponse<ITypeFrais>;
export type EntityArrayResponseType = HttpResponse<ITypeFrais[]>;

@Injectable({ providedIn: 'root' })
export class TypeFraisService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-frais', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(typeFrais: NewTypeFrais): Observable<EntityResponseType> {
    return this.http.post<ITypeFrais>(this.resourceUrl, typeFrais, { observe: 'response' });
  }

  update(typeFrais: ITypeFrais): Observable<EntityResponseType> {
    return this.http.put<ITypeFrais>(`${this.resourceUrl}/${this.getTypeFraisIdentifier(typeFrais)}`, typeFrais, { observe: 'response' });
  }

  partialUpdate(typeFrais: PartialUpdateTypeFrais): Observable<EntityResponseType> {
    return this.http.patch<ITypeFrais>(`${this.resourceUrl}/${this.getTypeFraisIdentifier(typeFrais)}`, typeFrais, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeFrais>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeFrais[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeFraisIdentifier(typeFrais: Pick<ITypeFrais, 'id'>): number {
    return typeFrais.id;
  }

  compareTypeFrais(o1: Pick<ITypeFrais, 'id'> | null, o2: Pick<ITypeFrais, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeFraisIdentifier(o1) === this.getTypeFraisIdentifier(o2) : o1 === o2;
  }

  addTypeFraisToCollectionIfMissing<Type extends Pick<ITypeFrais, 'id'>>(
    typeFraisCollection: Type[],
    ...typeFraisToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeFrais: Type[] = typeFraisToCheck.filter(isPresent);
    if (typeFrais.length > 0) {
      const typeFraisCollectionIdentifiers = typeFraisCollection.map(typeFraisItem => this.getTypeFraisIdentifier(typeFraisItem)!);
      const typeFraisToAdd = typeFrais.filter(typeFraisItem => {
        const typeFraisIdentifier = this.getTypeFraisIdentifier(typeFraisItem);
        if (typeFraisCollectionIdentifiers.includes(typeFraisIdentifier)) {
          return false;
        }
        typeFraisCollectionIdentifiers.push(typeFraisIdentifier);
        return true;
      });
      return [...typeFraisToAdd, ...typeFraisCollection];
    }
    return typeFraisCollection;
  }
}
