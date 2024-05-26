import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEnseignement, NewEnseignement } from '../enseignement.model';

export type PartialUpdateEnseignement = Partial<IEnseignement> & Pick<IEnseignement, 'id'>;

export type EntityResponseType = HttpResponse<IEnseignement>;
export type EntityArrayResponseType = HttpResponse<IEnseignement[]>;

@Injectable({ providedIn: 'root' })
export class EnseignementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/enseignements', 'microserviceenseignement');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(enseignement: NewEnseignement): Observable<EntityResponseType> {
    return this.http.post<IEnseignement>(this.resourceUrl, enseignement, { observe: 'response' });
  }

  update(enseignement: IEnseignement): Observable<EntityResponseType> {
    return this.http.put<IEnseignement>(`${this.resourceUrl}/${this.getEnseignementIdentifier(enseignement)}`, enseignement, {
      observe: 'response',
    });
  }

  partialUpdate(enseignement: PartialUpdateEnseignement): Observable<EntityResponseType> {
    return this.http.patch<IEnseignement>(`${this.resourceUrl}/${this.getEnseignementIdentifier(enseignement)}`, enseignement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEnseignement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEnseignement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEnseignementIdentifier(enseignement: Pick<IEnseignement, 'id'>): number {
    return enseignement.id;
  }

  compareEnseignement(o1: Pick<IEnseignement, 'id'> | null, o2: Pick<IEnseignement, 'id'> | null): boolean {
    return o1 && o2 ? this.getEnseignementIdentifier(o1) === this.getEnseignementIdentifier(o2) : o1 === o2;
  }

  addEnseignementToCollectionIfMissing<Type extends Pick<IEnseignement, 'id'>>(
    enseignementCollection: Type[],
    ...enseignementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const enseignements: Type[] = enseignementsToCheck.filter(isPresent);
    if (enseignements.length > 0) {
      const enseignementCollectionIdentifiers = enseignementCollection.map(
        enseignementItem => this.getEnseignementIdentifier(enseignementItem)!,
      );
      const enseignementsToAdd = enseignements.filter(enseignementItem => {
        const enseignementIdentifier = this.getEnseignementIdentifier(enseignementItem);
        if (enseignementCollectionIdentifiers.includes(enseignementIdentifier)) {
          return false;
        }
        enseignementCollectionIdentifiers.push(enseignementIdentifier);
        return true;
      });
      return [...enseignementsToAdd, ...enseignementCollection];
    }
    return enseignementCollection;
  }
}
