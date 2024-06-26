import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IFormation, NewFormation } from '../formation.model';

export type PartialUpdateFormation = Partial<IFormation> & Pick<IFormation, 'id'>;

export type EntityResponseType = HttpResponse<IFormation>;
export type EntityArrayResponseType = HttpResponse<IFormation[]>;

@Injectable({ providedIn: 'root' })
export class FormationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formations', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/formations/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formation: NewFormation): Observable<EntityResponseType> {
    return this.http.post<IFormation>(this.resourceUrl, formation, { observe: 'response' });
  }

  update(formation: IFormation): Observable<EntityResponseType> {
    return this.http.put<IFormation>(`${this.resourceUrl}/${this.getFormationIdentifier(formation)}`, formation, { observe: 'response' });
  }

  partialUpdate(formation: PartialUpdateFormation): Observable<EntityResponseType> {
    return this.http.patch<IFormation>(`${this.resourceUrl}/${this.getFormationIdentifier(formation)}`, formation, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormation>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormation[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormation[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IFormation[]>()], asapScheduler)));
  }

  getFormationIdentifier(formation: Pick<IFormation, 'id'>): number {
    return formation.id;
  }

  compareFormation(o1: Pick<IFormation, 'id'> | null, o2: Pick<IFormation, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationIdentifier(o1) === this.getFormationIdentifier(o2) : o1 === o2;
  }

  addFormationToCollectionIfMissing<Type extends Pick<IFormation, 'id'>>(
    formationCollection: Type[],
    ...formationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formations: Type[] = formationsToCheck.filter(isPresent);
    if (formations.length > 0) {
      const formationCollectionIdentifiers = formationCollection.map(formationItem => this.getFormationIdentifier(formationItem)!);
      const formationsToAdd = formations.filter(formationItem => {
        const formationIdentifier = this.getFormationIdentifier(formationItem);
        if (formationCollectionIdentifiers.includes(formationIdentifier)) {
          return false;
        }
        formationCollectionIdentifiers.push(formationIdentifier);
        return true;
      });
      return [...formationsToAdd, ...formationCollection];
    }
    return formationCollection;
  }
}
