import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInscriptionDoctorat, NewInscriptionDoctorat } from '../inscription-doctorat.model';

export type PartialUpdateInscriptionDoctorat = Partial<IInscriptionDoctorat> & Pick<IInscriptionDoctorat, 'id'>;

export type EntityResponseType = HttpResponse<IInscriptionDoctorat>;
export type EntityArrayResponseType = HttpResponse<IInscriptionDoctorat[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionDoctoratService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscription-doctorats', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/inscription-doctorats/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(inscriptionDoctorat: NewInscriptionDoctorat): Observable<EntityResponseType> {
    return this.http.post<IInscriptionDoctorat>(this.resourceUrl, inscriptionDoctorat, { observe: 'response' });
  }

  update(inscriptionDoctorat: IInscriptionDoctorat): Observable<EntityResponseType> {
    return this.http.put<IInscriptionDoctorat>(
      `${this.resourceUrl}/${this.getInscriptionDoctoratIdentifier(inscriptionDoctorat)}`,
      inscriptionDoctorat,
      { observe: 'response' },
    );
  }

  partialUpdate(inscriptionDoctorat: PartialUpdateInscriptionDoctorat): Observable<EntityResponseType> {
    return this.http.patch<IInscriptionDoctorat>(
      `${this.resourceUrl}/${this.getInscriptionDoctoratIdentifier(inscriptionDoctorat)}`,
      inscriptionDoctorat,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInscriptionDoctorat>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInscriptionDoctorat[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInscriptionDoctorat[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IInscriptionDoctorat[]>()], asapScheduler)));
  }

  getInscriptionDoctoratIdentifier(inscriptionDoctorat: Pick<IInscriptionDoctorat, 'id'>): number {
    return inscriptionDoctorat.id;
  }

  compareInscriptionDoctorat(o1: Pick<IInscriptionDoctorat, 'id'> | null, o2: Pick<IInscriptionDoctorat, 'id'> | null): boolean {
    return o1 && o2 ? this.getInscriptionDoctoratIdentifier(o1) === this.getInscriptionDoctoratIdentifier(o2) : o1 === o2;
  }

  addInscriptionDoctoratToCollectionIfMissing<Type extends Pick<IInscriptionDoctorat, 'id'>>(
    inscriptionDoctoratCollection: Type[],
    ...inscriptionDoctoratsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inscriptionDoctorats: Type[] = inscriptionDoctoratsToCheck.filter(isPresent);
    if (inscriptionDoctorats.length > 0) {
      const inscriptionDoctoratCollectionIdentifiers = inscriptionDoctoratCollection.map(
        inscriptionDoctoratItem => this.getInscriptionDoctoratIdentifier(inscriptionDoctoratItem)!,
      );
      const inscriptionDoctoratsToAdd = inscriptionDoctorats.filter(inscriptionDoctoratItem => {
        const inscriptionDoctoratIdentifier = this.getInscriptionDoctoratIdentifier(inscriptionDoctoratItem);
        if (inscriptionDoctoratCollectionIdentifiers.includes(inscriptionDoctoratIdentifier)) {
          return false;
        }
        inscriptionDoctoratCollectionIdentifiers.push(inscriptionDoctoratIdentifier);
        return true;
      });
      return [...inscriptionDoctoratsToAdd, ...inscriptionDoctoratCollection];
    }
    return inscriptionDoctoratCollection;
  }
}
