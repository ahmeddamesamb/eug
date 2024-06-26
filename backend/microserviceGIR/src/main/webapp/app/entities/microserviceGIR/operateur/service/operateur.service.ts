import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IOperateur, NewOperateur } from '../operateur.model';

export type PartialUpdateOperateur = Partial<IOperateur> & Pick<IOperateur, 'id'>;

export type EntityResponseType = HttpResponse<IOperateur>;
export type EntityArrayResponseType = HttpResponse<IOperateur[]>;

@Injectable({ providedIn: 'root' })
export class OperateurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/operateurs', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/operateurs/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(operateur: NewOperateur): Observable<EntityResponseType> {
    return this.http.post<IOperateur>(this.resourceUrl, operateur, { observe: 'response' });
  }

  update(operateur: IOperateur): Observable<EntityResponseType> {
    return this.http.put<IOperateur>(`${this.resourceUrl}/${this.getOperateurIdentifier(operateur)}`, operateur, { observe: 'response' });
  }

  partialUpdate(operateur: PartialUpdateOperateur): Observable<EntityResponseType> {
    return this.http.patch<IOperateur>(`${this.resourceUrl}/${this.getOperateurIdentifier(operateur)}`, operateur, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOperateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOperateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IOperateur[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IOperateur[]>()], asapScheduler)));
  }

  getOperateurIdentifier(operateur: Pick<IOperateur, 'id'>): number {
    return operateur.id;
  }

  compareOperateur(o1: Pick<IOperateur, 'id'> | null, o2: Pick<IOperateur, 'id'> | null): boolean {
    return o1 && o2 ? this.getOperateurIdentifier(o1) === this.getOperateurIdentifier(o2) : o1 === o2;
  }

  addOperateurToCollectionIfMissing<Type extends Pick<IOperateur, 'id'>>(
    operateurCollection: Type[],
    ...operateursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const operateurs: Type[] = operateursToCheck.filter(isPresent);
    if (operateurs.length > 0) {
      const operateurCollectionIdentifiers = operateurCollection.map(operateurItem => this.getOperateurIdentifier(operateurItem)!);
      const operateursToAdd = operateurs.filter(operateurItem => {
        const operateurIdentifier = this.getOperateurIdentifier(operateurItem);
        if (operateurCollectionIdentifiers.includes(operateurIdentifier)) {
          return false;
        }
        operateurCollectionIdentifiers.push(operateurIdentifier);
        return true;
      });
      return [...operateursToAdd, ...operateurCollection];
    }
    return operateurCollection;
  }
}
