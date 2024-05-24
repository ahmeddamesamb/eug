import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormationPrivee, NewFormationPrivee } from '../formation-privee.model';

export type PartialUpdateFormationPrivee = Partial<IFormationPrivee> & Pick<IFormationPrivee, 'id'>;

export type EntityResponseType = HttpResponse<IFormationPrivee>;
export type EntityArrayResponseType = HttpResponse<IFormationPrivee[]>;

@Injectable({ providedIn: 'root' })
export class FormationPriveeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-privees', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formationPrivee: NewFormationPrivee): Observable<EntityResponseType> {
    return this.http.post<IFormationPrivee>(this.resourceUrl, formationPrivee, { observe: 'response' });
  }

  update(formationPrivee: IFormationPrivee): Observable<EntityResponseType> {
    return this.http.put<IFormationPrivee>(`${this.resourceUrl}/${this.getFormationPriveeIdentifier(formationPrivee)}`, formationPrivee, {
      observe: 'response',
    });
  }

  partialUpdate(formationPrivee: PartialUpdateFormationPrivee): Observable<EntityResponseType> {
    return this.http.patch<IFormationPrivee>(`${this.resourceUrl}/${this.getFormationPriveeIdentifier(formationPrivee)}`, formationPrivee, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormationPrivee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormationPrivee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormationPriveeIdentifier(formationPrivee: Pick<IFormationPrivee, 'id'>): number {
    return formationPrivee.id;
  }

  compareFormationPrivee(o1: Pick<IFormationPrivee, 'id'> | null, o2: Pick<IFormationPrivee, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationPriveeIdentifier(o1) === this.getFormationPriveeIdentifier(o2) : o1 === o2;
  }

  addFormationPriveeToCollectionIfMissing<Type extends Pick<IFormationPrivee, 'id'>>(
    formationPriveeCollection: Type[],
    ...formationPriveesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formationPrivees: Type[] = formationPriveesToCheck.filter(isPresent);
    if (formationPrivees.length > 0) {
      const formationPriveeCollectionIdentifiers = formationPriveeCollection.map(
        formationPriveeItem => this.getFormationPriveeIdentifier(formationPriveeItem)!,
      );
      const formationPriveesToAdd = formationPrivees.filter(formationPriveeItem => {
        const formationPriveeIdentifier = this.getFormationPriveeIdentifier(formationPriveeItem);
        if (formationPriveeCollectionIdentifiers.includes(formationPriveeIdentifier)) {
          return false;
        }
        formationPriveeCollectionIdentifiers.push(formationPriveeIdentifier);
        return true;
      });
      return [...formationPriveesToAdd, ...formationPriveeCollection];
    }
    return formationPriveeCollection;
  }
}
