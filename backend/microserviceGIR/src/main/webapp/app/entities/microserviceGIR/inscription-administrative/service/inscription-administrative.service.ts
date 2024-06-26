import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInscriptionAdministrative, NewInscriptionAdministrative } from '../inscription-administrative.model';

export type PartialUpdateInscriptionAdministrative = Partial<IInscriptionAdministrative> & Pick<IInscriptionAdministrative, 'id'>;

export type EntityResponseType = HttpResponse<IInscriptionAdministrative>;
export type EntityArrayResponseType = HttpResponse<IInscriptionAdministrative[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionAdministrativeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscription-administratives', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/inscription-administratives/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(inscriptionAdministrative: NewInscriptionAdministrative): Observable<EntityResponseType> {
    return this.http.post<IInscriptionAdministrative>(this.resourceUrl, inscriptionAdministrative, { observe: 'response' });
  }

  update(inscriptionAdministrative: IInscriptionAdministrative): Observable<EntityResponseType> {
    return this.http.put<IInscriptionAdministrative>(
      `${this.resourceUrl}/${this.getInscriptionAdministrativeIdentifier(inscriptionAdministrative)}`,
      inscriptionAdministrative,
      { observe: 'response' },
    );
  }

  partialUpdate(inscriptionAdministrative: PartialUpdateInscriptionAdministrative): Observable<EntityResponseType> {
    return this.http.patch<IInscriptionAdministrative>(
      `${this.resourceUrl}/${this.getInscriptionAdministrativeIdentifier(inscriptionAdministrative)}`,
      inscriptionAdministrative,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscriptionAdministrative>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscriptionAdministrative[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInscriptionAdministrative[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IInscriptionAdministrative[]>()], asapScheduler)));
  }

  getInscriptionAdministrativeIdentifier(inscriptionAdministrative: Pick<IInscriptionAdministrative, 'id'>): number {
    return inscriptionAdministrative.id;
  }

  compareInscriptionAdministrative(
    o1: Pick<IInscriptionAdministrative, 'id'> | null,
    o2: Pick<IInscriptionAdministrative, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getInscriptionAdministrativeIdentifier(o1) === this.getInscriptionAdministrativeIdentifier(o2) : o1 === o2;
  }

  addInscriptionAdministrativeToCollectionIfMissing<Type extends Pick<IInscriptionAdministrative, 'id'>>(
    inscriptionAdministrativeCollection: Type[],
    ...inscriptionAdministrativesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inscriptionAdministratives: Type[] = inscriptionAdministrativesToCheck.filter(isPresent);
    if (inscriptionAdministratives.length > 0) {
      const inscriptionAdministrativeCollectionIdentifiers = inscriptionAdministrativeCollection.map(
        inscriptionAdministrativeItem => this.getInscriptionAdministrativeIdentifier(inscriptionAdministrativeItem)!,
      );
      const inscriptionAdministrativesToAdd = inscriptionAdministratives.filter(inscriptionAdministrativeItem => {
        const inscriptionAdministrativeIdentifier = this.getInscriptionAdministrativeIdentifier(inscriptionAdministrativeItem);
        if (inscriptionAdministrativeCollectionIdentifiers.includes(inscriptionAdministrativeIdentifier)) {
          return false;
        }
        inscriptionAdministrativeCollectionIdentifiers.push(inscriptionAdministrativeIdentifier);
        return true;
      });
      return [...inscriptionAdministrativesToAdd, ...inscriptionAdministrativeCollection];
    }
    return inscriptionAdministrativeCollection;
  }
}
