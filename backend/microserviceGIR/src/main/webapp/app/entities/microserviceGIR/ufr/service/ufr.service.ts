import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IUfr, NewUfr } from '../ufr.model';

export type PartialUpdateUfr = Partial<IUfr> & Pick<IUfr, 'id'>;

export type EntityResponseType = HttpResponse<IUfr>;
export type EntityArrayResponseType = HttpResponse<IUfr[]>;

@Injectable({ providedIn: 'root' })
export class UfrService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ufrs', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/ufrs/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(ufr: NewUfr): Observable<EntityResponseType> {
    return this.http.post<IUfr>(this.resourceUrl, ufr, { observe: 'response' });
  }

  update(ufr: IUfr): Observable<EntityResponseType> {
    return this.http.put<IUfr>(`${this.resourceUrl}/${this.getUfrIdentifier(ufr)}`, ufr, { observe: 'response' });
  }

  partialUpdate(ufr: PartialUpdateUfr): Observable<EntityResponseType> {
    return this.http.patch<IUfr>(`${this.resourceUrl}/${this.getUfrIdentifier(ufr)}`, ufr, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUfr>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUfr[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUfr[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IUfr[]>()], asapScheduler)));
  }

  getUfrIdentifier(ufr: Pick<IUfr, 'id'>): number {
    return ufr.id;
  }

  compareUfr(o1: Pick<IUfr, 'id'> | null, o2: Pick<IUfr, 'id'> | null): boolean {
    return o1 && o2 ? this.getUfrIdentifier(o1) === this.getUfrIdentifier(o2) : o1 === o2;
  }

  addUfrToCollectionIfMissing<Type extends Pick<IUfr, 'id'>>(ufrCollection: Type[], ...ufrsToCheck: (Type | null | undefined)[]): Type[] {
    const ufrs: Type[] = ufrsToCheck.filter(isPresent);
    if (ufrs.length > 0) {
      const ufrCollectionIdentifiers = ufrCollection.map(ufrItem => this.getUfrIdentifier(ufrItem)!);
      const ufrsToAdd = ufrs.filter(ufrItem => {
        const ufrIdentifier = this.getUfrIdentifier(ufrItem);
        if (ufrCollectionIdentifiers.includes(ufrIdentifier)) {
          return false;
        }
        ufrCollectionIdentifiers.push(ufrIdentifier);
        return true;
      });
      return [...ufrsToAdd, ...ufrCollection];
    }
    return ufrCollection;
  }
}
