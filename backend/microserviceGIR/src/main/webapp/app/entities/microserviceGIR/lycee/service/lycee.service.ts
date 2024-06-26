import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ILycee, NewLycee } from '../lycee.model';

export type PartialUpdateLycee = Partial<ILycee> & Pick<ILycee, 'id'>;

export type EntityResponseType = HttpResponse<ILycee>;
export type EntityArrayResponseType = HttpResponse<ILycee[]>;

@Injectable({ providedIn: 'root' })
export class LyceeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lycees', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/lycees/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(lycee: NewLycee): Observable<EntityResponseType> {
    return this.http.post<ILycee>(this.resourceUrl, lycee, { observe: 'response' });
  }

  update(lycee: ILycee): Observable<EntityResponseType> {
    return this.http.put<ILycee>(`${this.resourceUrl}/${this.getLyceeIdentifier(lycee)}`, lycee, { observe: 'response' });
  }

  partialUpdate(lycee: PartialUpdateLycee): Observable<EntityResponseType> {
    return this.http.patch<ILycee>(`${this.resourceUrl}/${this.getLyceeIdentifier(lycee)}`, lycee, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILycee>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILycee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ILycee[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<ILycee[]>()], asapScheduler)));
  }

  getLyceeIdentifier(lycee: Pick<ILycee, 'id'>): number {
    return lycee.id;
  }

  compareLycee(o1: Pick<ILycee, 'id'> | null, o2: Pick<ILycee, 'id'> | null): boolean {
    return o1 && o2 ? this.getLyceeIdentifier(o1) === this.getLyceeIdentifier(o2) : o1 === o2;
  }

  addLyceeToCollectionIfMissing<Type extends Pick<ILycee, 'id'>>(
    lyceeCollection: Type[],
    ...lyceesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const lycees: Type[] = lyceesToCheck.filter(isPresent);
    if (lycees.length > 0) {
      const lyceeCollectionIdentifiers = lyceeCollection.map(lyceeItem => this.getLyceeIdentifier(lyceeItem)!);
      const lyceesToAdd = lycees.filter(lyceeItem => {
        const lyceeIdentifier = this.getLyceeIdentifier(lyceeItem);
        if (lyceeCollectionIdentifiers.includes(lyceeIdentifier)) {
          return false;
        }
        lyceeCollectionIdentifiers.push(lyceeIdentifier);
        return true;
      });
      return [...lyceesToAdd, ...lyceeCollection];
    }
    return lyceeCollection;
  }
}
