import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUFR, NewUFR } from '../ufr.model';

export type PartialUpdateUFR = Partial<IUFR> & Pick<IUFR, 'id'>;

export type EntityResponseType = HttpResponse<IUFR>;
export type EntityArrayResponseType = HttpResponse<IUFR[]>;

@Injectable({ providedIn: 'root' })
export class UFRService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ufrs', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(uFR: NewUFR): Observable<EntityResponseType> {
    return this.http.post<IUFR>(this.resourceUrl, uFR, { observe: 'response' });
  }

  update(uFR: IUFR): Observable<EntityResponseType> {
    return this.http.put<IUFR>(`${this.resourceUrl}/${this.getUFRIdentifier(uFR)}`, uFR, { observe: 'response' });
  }

  partialUpdate(uFR: PartialUpdateUFR): Observable<EntityResponseType> {
    return this.http.patch<IUFR>(`${this.resourceUrl}/${this.getUFRIdentifier(uFR)}`, uFR, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUFR>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUFR[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUFRIdentifier(uFR: Pick<IUFR, 'id'>): number {
    return uFR.id;
  }

  compareUFR(o1: Pick<IUFR, 'id'> | null, o2: Pick<IUFR, 'id'> | null): boolean {
    return o1 && o2 ? this.getUFRIdentifier(o1) === this.getUFRIdentifier(o2) : o1 === o2;
  }

  addUFRToCollectionIfMissing<Type extends Pick<IUFR, 'id'>>(uFRCollection: Type[], ...uFRSToCheck: (Type | null | undefined)[]): Type[] {
    const uFRS: Type[] = uFRSToCheck.filter(isPresent);
    if (uFRS.length > 0) {
      const uFRCollectionIdentifiers = uFRCollection.map(uFRItem => this.getUFRIdentifier(uFRItem)!);
      const uFRSToAdd = uFRS.filter(uFRItem => {
        const uFRIdentifier = this.getUFRIdentifier(uFRItem);
        if (uFRCollectionIdentifiers.includes(uFRIdentifier)) {
          return false;
        }
        uFRCollectionIdentifiers.push(uFRIdentifier);
        return true;
      });
      return [...uFRSToAdd, ...uFRCollection];
    }
    return uFRCollection;
  }
}
