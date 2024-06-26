import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDisciplineSportive, NewDisciplineSportive } from '../discipline-sportive.model';

export type PartialUpdateDisciplineSportive = Partial<IDisciplineSportive> & Pick<IDisciplineSportive, 'id'>;

export type EntityResponseType = HttpResponse<IDisciplineSportive>;
export type EntityArrayResponseType = HttpResponse<IDisciplineSportive[]>;

@Injectable({ providedIn: 'root' })
export class DisciplineSportiveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/discipline-sportives', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/discipline-sportives/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(disciplineSportive: NewDisciplineSportive): Observable<EntityResponseType> {
    return this.http.post<IDisciplineSportive>(this.resourceUrl, disciplineSportive, { observe: 'response' });
  }

  update(disciplineSportive: IDisciplineSportive): Observable<EntityResponseType> {
    return this.http.put<IDisciplineSportive>(
      `${this.resourceUrl}/${this.getDisciplineSportiveIdentifier(disciplineSportive)}`,
      disciplineSportive,
      { observe: 'response' },
    );
  }

  partialUpdate(disciplineSportive: PartialUpdateDisciplineSportive): Observable<EntityResponseType> {
    return this.http.patch<IDisciplineSportive>(
      `${this.resourceUrl}/${this.getDisciplineSportiveIdentifier(disciplineSportive)}`,
      disciplineSportive,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisciplineSportive>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisciplineSportive[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDisciplineSportive[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IDisciplineSportive[]>()], asapScheduler)));
  }

  getDisciplineSportiveIdentifier(disciplineSportive: Pick<IDisciplineSportive, 'id'>): number {
    return disciplineSportive.id;
  }

  compareDisciplineSportive(o1: Pick<IDisciplineSportive, 'id'> | null, o2: Pick<IDisciplineSportive, 'id'> | null): boolean {
    return o1 && o2 ? this.getDisciplineSportiveIdentifier(o1) === this.getDisciplineSportiveIdentifier(o2) : o1 === o2;
  }

  addDisciplineSportiveToCollectionIfMissing<Type extends Pick<IDisciplineSportive, 'id'>>(
    disciplineSportiveCollection: Type[],
    ...disciplineSportivesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const disciplineSportives: Type[] = disciplineSportivesToCheck.filter(isPresent);
    if (disciplineSportives.length > 0) {
      const disciplineSportiveCollectionIdentifiers = disciplineSportiveCollection.map(
        disciplineSportiveItem => this.getDisciplineSportiveIdentifier(disciplineSportiveItem)!,
      );
      const disciplineSportivesToAdd = disciplineSportives.filter(disciplineSportiveItem => {
        const disciplineSportiveIdentifier = this.getDisciplineSportiveIdentifier(disciplineSportiveItem);
        if (disciplineSportiveCollectionIdentifiers.includes(disciplineSportiveIdentifier)) {
          return false;
        }
        disciplineSportiveCollectionIdentifiers.push(disciplineSportiveIdentifier);
        return true;
      });
      return [...disciplineSportivesToAdd, ...disciplineSportiveCollection];
    }
    return disciplineSportiveCollection;
  }
}
