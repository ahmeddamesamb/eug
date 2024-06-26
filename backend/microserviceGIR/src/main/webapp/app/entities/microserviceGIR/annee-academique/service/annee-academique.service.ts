import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IAnneeAcademique, NewAnneeAcademique } from '../annee-academique.model';

export type PartialUpdateAnneeAcademique = Partial<IAnneeAcademique> & Pick<IAnneeAcademique, 'id'>;

export type EntityResponseType = HttpResponse<IAnneeAcademique>;
export type EntityArrayResponseType = HttpResponse<IAnneeAcademique[]>;

@Injectable({ providedIn: 'root' })
export class AnneeAcademiqueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annee-academiques', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/annee-academiques/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(anneeAcademique: NewAnneeAcademique): Observable<EntityResponseType> {
    return this.http.post<IAnneeAcademique>(this.resourceUrl, anneeAcademique, { observe: 'response' });
  }

  update(anneeAcademique: IAnneeAcademique): Observable<EntityResponseType> {
    return this.http.put<IAnneeAcademique>(`${this.resourceUrl}/${this.getAnneeAcademiqueIdentifier(anneeAcademique)}`, anneeAcademique, {
      observe: 'response',
    });
  }

  partialUpdate(anneeAcademique: PartialUpdateAnneeAcademique): Observable<EntityResponseType> {
    return this.http.patch<IAnneeAcademique>(`${this.resourceUrl}/${this.getAnneeAcademiqueIdentifier(anneeAcademique)}`, anneeAcademique, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnneeAcademique>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnneeAcademique[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAnneeAcademique[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IAnneeAcademique[]>()], asapScheduler)));
  }

  getAnneeAcademiqueIdentifier(anneeAcademique: Pick<IAnneeAcademique, 'id'>): number {
    return anneeAcademique.id;
  }

  compareAnneeAcademique(o1: Pick<IAnneeAcademique, 'id'> | null, o2: Pick<IAnneeAcademique, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnneeAcademiqueIdentifier(o1) === this.getAnneeAcademiqueIdentifier(o2) : o1 === o2;
  }

  addAnneeAcademiqueToCollectionIfMissing<Type extends Pick<IAnneeAcademique, 'id'>>(
    anneeAcademiqueCollection: Type[],
    ...anneeAcademiquesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const anneeAcademiques: Type[] = anneeAcademiquesToCheck.filter(isPresent);
    if (anneeAcademiques.length > 0) {
      const anneeAcademiqueCollectionIdentifiers = anneeAcademiqueCollection.map(
        anneeAcademiqueItem => this.getAnneeAcademiqueIdentifier(anneeAcademiqueItem)!,
      );
      const anneeAcademiquesToAdd = anneeAcademiques.filter(anneeAcademiqueItem => {
        const anneeAcademiqueIdentifier = this.getAnneeAcademiqueIdentifier(anneeAcademiqueItem);
        if (anneeAcademiqueCollectionIdentifiers.includes(anneeAcademiqueIdentifier)) {
          return false;
        }
        anneeAcademiqueCollectionIdentifiers.push(anneeAcademiqueIdentifier);
        return true;
      });
      return [...anneeAcademiquesToAdd, ...anneeAcademiqueCollection];
    }
    return anneeAcademiqueCollection;
  }
}
