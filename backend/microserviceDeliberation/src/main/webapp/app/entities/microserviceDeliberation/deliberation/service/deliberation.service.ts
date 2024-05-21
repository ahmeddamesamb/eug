import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliberation, NewDeliberation } from '../deliberation.model';

export type PartialUpdateDeliberation = Partial<IDeliberation> & Pick<IDeliberation, 'id'>;

export type EntityResponseType = HttpResponse<IDeliberation>;
export type EntityArrayResponseType = HttpResponse<IDeliberation[]>;

@Injectable({ providedIn: 'root' })
export class DeliberationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/deliberations', 'microservicedeliberation');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(deliberation: NewDeliberation): Observable<EntityResponseType> {
    return this.http.post<IDeliberation>(this.resourceUrl, deliberation, { observe: 'response' });
  }

  update(deliberation: IDeliberation): Observable<EntityResponseType> {
    return this.http.put<IDeliberation>(`${this.resourceUrl}/${this.getDeliberationIdentifier(deliberation)}`, deliberation, {
      observe: 'response',
    });
  }

  partialUpdate(deliberation: PartialUpdateDeliberation): Observable<EntityResponseType> {
    return this.http.patch<IDeliberation>(`${this.resourceUrl}/${this.getDeliberationIdentifier(deliberation)}`, deliberation, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeliberation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeliberation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDeliberationIdentifier(deliberation: Pick<IDeliberation, 'id'>): number {
    return deliberation.id;
  }

  compareDeliberation(o1: Pick<IDeliberation, 'id'> | null, o2: Pick<IDeliberation, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeliberationIdentifier(o1) === this.getDeliberationIdentifier(o2) : o1 === o2;
  }

  addDeliberationToCollectionIfMissing<Type extends Pick<IDeliberation, 'id'>>(
    deliberationCollection: Type[],
    ...deliberationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const deliberations: Type[] = deliberationsToCheck.filter(isPresent);
    if (deliberations.length > 0) {
      const deliberationCollectionIdentifiers = deliberationCollection.map(
        deliberationItem => this.getDeliberationIdentifier(deliberationItem)!,
      );
      const deliberationsToAdd = deliberations.filter(deliberationItem => {
        const deliberationIdentifier = this.getDeliberationIdentifier(deliberationItem);
        if (deliberationCollectionIdentifiers.includes(deliberationIdentifier)) {
          return false;
        }
        deliberationCollectionIdentifiers.push(deliberationIdentifier);
        return true;
      });
      return [...deliberationsToAdd, ...deliberationCollection];
    }
    return deliberationCollection;
  }
}
