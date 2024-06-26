import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProcessusInscriptionAdministrative, NewProcessusInscriptionAdministrative } from '../processus-inscription-administrative.model';

export type PartialUpdateProcessusInscriptionAdministrative = Partial<IProcessusInscriptionAdministrative> &
  Pick<IProcessusInscriptionAdministrative, 'id'>;

type RestOf<T extends IProcessusInscriptionAdministrative | NewProcessusInscriptionAdministrative> = Omit<
  T,
  | 'dateDemarrageInscription'
  | 'dateAnnulationInscription'
  | 'dateVisiteMedicale'
  | 'dateRemiseQuitusCROUS'
  | 'dateRemiseQuitusBU'
  | 'datePaiementFraisObligatoires'
  | 'dateRemiseCarteEtu'
  | 'dateInscriptionAdministrative'
  | 'derniereModification'
> & {
  dateDemarrageInscription?: string | null;
  dateAnnulationInscription?: string | null;
  dateVisiteMedicale?: string | null;
  dateRemiseQuitusCROUS?: string | null;
  dateRemiseQuitusBU?: string | null;
  datePaiementFraisObligatoires?: string | null;
  dateRemiseCarteEtu?: string | null;
  dateInscriptionAdministrative?: string | null;
  derniereModification?: string | null;
};

export type RestProcessusInscriptionAdministrative = RestOf<IProcessusInscriptionAdministrative>;

export type NewRestProcessusInscriptionAdministrative = RestOf<NewProcessusInscriptionAdministrative>;

export type PartialUpdateRestProcessusInscriptionAdministrative = RestOf<PartialUpdateProcessusInscriptionAdministrative>;

export type EntityResponseType = HttpResponse<IProcessusInscriptionAdministrative>;
export type EntityArrayResponseType = HttpResponse<IProcessusInscriptionAdministrative[]>;

@Injectable({ providedIn: 'root' })
export class ProcessusInscriptionAdministrativeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/processus-inscription-administratives', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/processus-inscription-administratives/_search',
    'microservicegir',
  );

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(processusInscriptionAdministrative: NewProcessusInscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusInscriptionAdministrative);
    return this.http
      .post<RestProcessusInscriptionAdministrative>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(processusInscriptionAdministrative: IProcessusInscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusInscriptionAdministrative);
    return this.http
      .put<RestProcessusInscriptionAdministrative>(
        `${this.resourceUrl}/${this.getProcessusInscriptionAdministrativeIdentifier(processusInscriptionAdministrative)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(processusInscriptionAdministrative: PartialUpdateProcessusInscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusInscriptionAdministrative);
    return this.http
      .patch<RestProcessusInscriptionAdministrative>(
        `${this.resourceUrl}/${this.getProcessusInscriptionAdministrativeIdentifier(processusInscriptionAdministrative)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcessusInscriptionAdministrative>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcessusInscriptionAdministrative[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestProcessusInscriptionAdministrative[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IProcessusInscriptionAdministrative[]>()], asapScheduler)),
    );
  }

  getProcessusInscriptionAdministrativeIdentifier(
    processusInscriptionAdministrative: Pick<IProcessusInscriptionAdministrative, 'id'>,
  ): number {
    return processusInscriptionAdministrative.id;
  }

  compareProcessusInscriptionAdministrative(
    o1: Pick<IProcessusInscriptionAdministrative, 'id'> | null,
    o2: Pick<IProcessusInscriptionAdministrative, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getProcessusInscriptionAdministrativeIdentifier(o1) === this.getProcessusInscriptionAdministrativeIdentifier(o2)
      : o1 === o2;
  }

  addProcessusInscriptionAdministrativeToCollectionIfMissing<Type extends Pick<IProcessusInscriptionAdministrative, 'id'>>(
    processusInscriptionAdministrativeCollection: Type[],
    ...processusInscriptionAdministrativesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const processusInscriptionAdministratives: Type[] = processusInscriptionAdministrativesToCheck.filter(isPresent);
    if (processusInscriptionAdministratives.length > 0) {
      const processusInscriptionAdministrativeCollectionIdentifiers = processusInscriptionAdministrativeCollection.map(
        processusInscriptionAdministrativeItem =>
          this.getProcessusInscriptionAdministrativeIdentifier(processusInscriptionAdministrativeItem)!,
      );
      const processusInscriptionAdministrativesToAdd = processusInscriptionAdministratives.filter(
        processusInscriptionAdministrativeItem => {
          const processusInscriptionAdministrativeIdentifier = this.getProcessusInscriptionAdministrativeIdentifier(
            processusInscriptionAdministrativeItem,
          );
          if (processusInscriptionAdministrativeCollectionIdentifiers.includes(processusInscriptionAdministrativeIdentifier)) {
            return false;
          }
          processusInscriptionAdministrativeCollectionIdentifiers.push(processusInscriptionAdministrativeIdentifier);
          return true;
        },
      );
      return [...processusInscriptionAdministrativesToAdd, ...processusInscriptionAdministrativeCollection];
    }
    return processusInscriptionAdministrativeCollection;
  }

  protected convertDateFromClient<
    T extends IProcessusInscriptionAdministrative | NewProcessusInscriptionAdministrative | PartialUpdateProcessusInscriptionAdministrative,
  >(processusInscriptionAdministrative: T): RestOf<T> {
    return {
      ...processusInscriptionAdministrative,
      dateDemarrageInscription: processusInscriptionAdministrative.dateDemarrageInscription?.toJSON() ?? null,
      dateAnnulationInscription: processusInscriptionAdministrative.dateAnnulationInscription?.toJSON() ?? null,
      dateVisiteMedicale: processusInscriptionAdministrative.dateVisiteMedicale?.toJSON() ?? null,
      dateRemiseQuitusCROUS: processusInscriptionAdministrative.dateRemiseQuitusCROUS?.toJSON() ?? null,
      dateRemiseQuitusBU: processusInscriptionAdministrative.dateRemiseQuitusBU?.toJSON() ?? null,
      datePaiementFraisObligatoires: processusInscriptionAdministrative.datePaiementFraisObligatoires?.toJSON() ?? null,
      dateRemiseCarteEtu: processusInscriptionAdministrative.dateRemiseCarteEtu?.toJSON() ?? null,
      dateInscriptionAdministrative: processusInscriptionAdministrative.dateInscriptionAdministrative?.toJSON() ?? null,
      derniereModification: processusInscriptionAdministrative.derniereModification?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(
    restProcessusInscriptionAdministrative: RestProcessusInscriptionAdministrative,
  ): IProcessusInscriptionAdministrative {
    return {
      ...restProcessusInscriptionAdministrative,
      dateDemarrageInscription: restProcessusInscriptionAdministrative.dateDemarrageInscription
        ? dayjs(restProcessusInscriptionAdministrative.dateDemarrageInscription)
        : undefined,
      dateAnnulationInscription: restProcessusInscriptionAdministrative.dateAnnulationInscription
        ? dayjs(restProcessusInscriptionAdministrative.dateAnnulationInscription)
        : undefined,
      dateVisiteMedicale: restProcessusInscriptionAdministrative.dateVisiteMedicale
        ? dayjs(restProcessusInscriptionAdministrative.dateVisiteMedicale)
        : undefined,
      dateRemiseQuitusCROUS: restProcessusInscriptionAdministrative.dateRemiseQuitusCROUS
        ? dayjs(restProcessusInscriptionAdministrative.dateRemiseQuitusCROUS)
        : undefined,
      dateRemiseQuitusBU: restProcessusInscriptionAdministrative.dateRemiseQuitusBU
        ? dayjs(restProcessusInscriptionAdministrative.dateRemiseQuitusBU)
        : undefined,
      datePaiementFraisObligatoires: restProcessusInscriptionAdministrative.datePaiementFraisObligatoires
        ? dayjs(restProcessusInscriptionAdministrative.datePaiementFraisObligatoires)
        : undefined,
      dateRemiseCarteEtu: restProcessusInscriptionAdministrative.dateRemiseCarteEtu
        ? dayjs(restProcessusInscriptionAdministrative.dateRemiseCarteEtu)
        : undefined,
      dateInscriptionAdministrative: restProcessusInscriptionAdministrative.dateInscriptionAdministrative
        ? dayjs(restProcessusInscriptionAdministrative.dateInscriptionAdministrative)
        : undefined,
      derniereModification: restProcessusInscriptionAdministrative.derniereModification
        ? dayjs(restProcessusInscriptionAdministrative.derniereModification)
        : undefined,
    };
  }

  protected convertResponseFromServer(
    res: HttpResponse<RestProcessusInscriptionAdministrative>,
  ): HttpResponse<IProcessusInscriptionAdministrative> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestProcessusInscriptionAdministrative[]>,
  ): HttpResponse<IProcessusInscriptionAdministrative[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
