import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormationInvalide, NewFormationInvalide } from '../formation-invalide.model';

export type PartialUpdateFormationInvalide = Partial<IFormationInvalide> & Pick<IFormationInvalide, 'id'>;

export type EntityResponseType = HttpResponse<IFormationInvalide>;
export type EntityArrayResponseType = HttpResponse<IFormationInvalide[]>;

@Injectable({ providedIn: 'root' })
export class FormationInvalideService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-invalides', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formationInvalide: NewFormationInvalide): Observable<EntityResponseType> {
    return this.http.post<IFormationInvalide>(this.resourceUrl, formationInvalide, { observe: 'response' });
  }

  update(formationInvalide: IFormationInvalide): Observable<EntityResponseType> {
    return this.http.put<IFormationInvalide>(
      `${this.resourceUrl}/${this.getFormationInvalideIdentifier(formationInvalide)}`,
      formationInvalide,
      { observe: 'response' },
    );
  }

  partialUpdate(formationInvalide: PartialUpdateFormationInvalide): Observable<EntityResponseType> {
    return this.http.patch<IFormationInvalide>(
      `${this.resourceUrl}/${this.getFormationInvalideIdentifier(formationInvalide)}`,
      formationInvalide,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormationInvalide>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormationInvalide[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormationInvalideIdentifier(formationInvalide: Pick<IFormationInvalide, 'id'>): number {
    return formationInvalide.id;
  }

  compareFormationInvalide(o1: Pick<IFormationInvalide, 'id'> | null, o2: Pick<IFormationInvalide, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationInvalideIdentifier(o1) === this.getFormationInvalideIdentifier(o2) : o1 === o2;
  }

  addFormationInvalideToCollectionIfMissing<Type extends Pick<IFormationInvalide, 'id'>>(
    formationInvalideCollection: Type[],
    ...formationInvalidesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formationInvalides: Type[] = formationInvalidesToCheck.filter(isPresent);
    if (formationInvalides.length > 0) {
      const formationInvalideCollectionIdentifiers = formationInvalideCollection.map(
        formationInvalideItem => this.getFormationInvalideIdentifier(formationInvalideItem)!,
      );
      const formationInvalidesToAdd = formationInvalides.filter(formationInvalideItem => {
        const formationInvalideIdentifier = this.getFormationInvalideIdentifier(formationInvalideItem);
        if (formationInvalideCollectionIdentifiers.includes(formationInvalideIdentifier)) {
          return false;
        }
        formationInvalideCollectionIdentifiers.push(formationInvalideIdentifier);
        return true;
      });
      return [...formationInvalidesToAdd, ...formationInvalideCollection];
    }
    return formationInvalideCollection;
  }
}
