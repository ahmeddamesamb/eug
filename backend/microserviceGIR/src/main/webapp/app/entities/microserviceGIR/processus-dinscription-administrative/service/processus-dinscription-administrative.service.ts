import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import {
  IProcessusDinscriptionAdministrative,
  NewProcessusDinscriptionAdministrative,
} from '../processus-dinscription-administrative.model';

export type PartialUpdateProcessusDinscriptionAdministrative = Partial<IProcessusDinscriptionAdministrative> &
  Pick<IProcessusDinscriptionAdministrative, 'id'>;

type RestOf<T extends IProcessusDinscriptionAdministrative | NewProcessusDinscriptionAdministrative> = Omit<
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

export type RestProcessusDinscriptionAdministrative = RestOf<IProcessusDinscriptionAdministrative>;

export type NewRestProcessusDinscriptionAdministrative = RestOf<NewProcessusDinscriptionAdministrative>;

export type PartialUpdateRestProcessusDinscriptionAdministrative = RestOf<PartialUpdateProcessusDinscriptionAdministrative>;

export type EntityResponseType = HttpResponse<IProcessusDinscriptionAdministrative>;
export type EntityArrayResponseType = HttpResponse<IProcessusDinscriptionAdministrative[]>;

@Injectable({ providedIn: 'root' })
export class ProcessusDinscriptionAdministrativeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/processus-dinscription-administratives', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(processusDinscriptionAdministrative: NewProcessusDinscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusDinscriptionAdministrative);
    return this.http
      .post<RestProcessusDinscriptionAdministrative>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(processusDinscriptionAdministrative: IProcessusDinscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusDinscriptionAdministrative);
    return this.http
      .put<RestProcessusDinscriptionAdministrative>(
        `${this.resourceUrl}/${this.getProcessusDinscriptionAdministrativeIdentifier(processusDinscriptionAdministrative)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(processusDinscriptionAdministrative: PartialUpdateProcessusDinscriptionAdministrative): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(processusDinscriptionAdministrative);
    return this.http
      .patch<RestProcessusDinscriptionAdministrative>(
        `${this.resourceUrl}/${this.getProcessusDinscriptionAdministrativeIdentifier(processusDinscriptionAdministrative)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProcessusDinscriptionAdministrative>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProcessusDinscriptionAdministrative[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProcessusDinscriptionAdministrativeIdentifier(
    processusDinscriptionAdministrative: Pick<IProcessusDinscriptionAdministrative, 'id'>,
  ): number {
    return processusDinscriptionAdministrative.id;
  }

  compareProcessusDinscriptionAdministrative(
    o1: Pick<IProcessusDinscriptionAdministrative, 'id'> | null,
    o2: Pick<IProcessusDinscriptionAdministrative, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getProcessusDinscriptionAdministrativeIdentifier(o1) === this.getProcessusDinscriptionAdministrativeIdentifier(o2)
      : o1 === o2;
  }

  addProcessusDinscriptionAdministrativeToCollectionIfMissing<Type extends Pick<IProcessusDinscriptionAdministrative, 'id'>>(
    processusDinscriptionAdministrativeCollection: Type[],
    ...processusDinscriptionAdministrativesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const processusDinscriptionAdministratives: Type[] = processusDinscriptionAdministrativesToCheck.filter(isPresent);
    if (processusDinscriptionAdministratives.length > 0) {
      const processusDinscriptionAdministrativeCollectionIdentifiers = processusDinscriptionAdministrativeCollection.map(
        processusDinscriptionAdministrativeItem =>
          this.getProcessusDinscriptionAdministrativeIdentifier(processusDinscriptionAdministrativeItem)!,
      );
      const processusDinscriptionAdministrativesToAdd = processusDinscriptionAdministratives.filter(
        processusDinscriptionAdministrativeItem => {
          const processusDinscriptionAdministrativeIdentifier = this.getProcessusDinscriptionAdministrativeIdentifier(
            processusDinscriptionAdministrativeItem,
          );
          if (processusDinscriptionAdministrativeCollectionIdentifiers.includes(processusDinscriptionAdministrativeIdentifier)) {
            return false;
          }
          processusDinscriptionAdministrativeCollectionIdentifiers.push(processusDinscriptionAdministrativeIdentifier);
          return true;
        },
      );
      return [...processusDinscriptionAdministrativesToAdd, ...processusDinscriptionAdministrativeCollection];
    }
    return processusDinscriptionAdministrativeCollection;
  }

  protected convertDateFromClient<
    T extends
      | IProcessusDinscriptionAdministrative
      | NewProcessusDinscriptionAdministrative
      | PartialUpdateProcessusDinscriptionAdministrative,
  >(processusDinscriptionAdministrative: T): RestOf<T> {
    return {
      ...processusDinscriptionAdministrative,
      dateDemarrageInscription: processusDinscriptionAdministrative.dateDemarrageInscription?.toJSON() ?? null,
      dateAnnulationInscription: processusDinscriptionAdministrative.dateAnnulationInscription?.toJSON() ?? null,
      dateVisiteMedicale: processusDinscriptionAdministrative.dateVisiteMedicale?.toJSON() ?? null,
      dateRemiseQuitusCROUS: processusDinscriptionAdministrative.dateRemiseQuitusCROUS?.toJSON() ?? null,
      dateRemiseQuitusBU: processusDinscriptionAdministrative.dateRemiseQuitusBU?.toJSON() ?? null,
      datePaiementFraisObligatoires: processusDinscriptionAdministrative.datePaiementFraisObligatoires?.toJSON() ?? null,
      dateRemiseCarteEtu: processusDinscriptionAdministrative.dateRemiseCarteEtu?.toJSON() ?? null,
      dateInscriptionAdministrative: processusDinscriptionAdministrative.dateInscriptionAdministrative?.toJSON() ?? null,
      derniereModification: processusDinscriptionAdministrative.derniereModification?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(
    restProcessusDinscriptionAdministrative: RestProcessusDinscriptionAdministrative,
  ): IProcessusDinscriptionAdministrative {
    return {
      ...restProcessusDinscriptionAdministrative,
      dateDemarrageInscription: restProcessusDinscriptionAdministrative.dateDemarrageInscription
        ? dayjs(restProcessusDinscriptionAdministrative.dateDemarrageInscription)
        : undefined,
      dateAnnulationInscription: restProcessusDinscriptionAdministrative.dateAnnulationInscription
        ? dayjs(restProcessusDinscriptionAdministrative.dateAnnulationInscription)
        : undefined,
      dateVisiteMedicale: restProcessusDinscriptionAdministrative.dateVisiteMedicale
        ? dayjs(restProcessusDinscriptionAdministrative.dateVisiteMedicale)
        : undefined,
      dateRemiseQuitusCROUS: restProcessusDinscriptionAdministrative.dateRemiseQuitusCROUS
        ? dayjs(restProcessusDinscriptionAdministrative.dateRemiseQuitusCROUS)
        : undefined,
      dateRemiseQuitusBU: restProcessusDinscriptionAdministrative.dateRemiseQuitusBU
        ? dayjs(restProcessusDinscriptionAdministrative.dateRemiseQuitusBU)
        : undefined,
      datePaiementFraisObligatoires: restProcessusDinscriptionAdministrative.datePaiementFraisObligatoires
        ? dayjs(restProcessusDinscriptionAdministrative.datePaiementFraisObligatoires)
        : undefined,
      dateRemiseCarteEtu: restProcessusDinscriptionAdministrative.dateRemiseCarteEtu
        ? dayjs(restProcessusDinscriptionAdministrative.dateRemiseCarteEtu)
        : undefined,
      dateInscriptionAdministrative: restProcessusDinscriptionAdministrative.dateInscriptionAdministrative
        ? dayjs(restProcessusDinscriptionAdministrative.dateInscriptionAdministrative)
        : undefined,
      derniereModification: restProcessusDinscriptionAdministrative.derniereModification
        ? dayjs(restProcessusDinscriptionAdministrative.derniereModification)
        : undefined,
    };
  }

  protected convertResponseFromServer(
    res: HttpResponse<RestProcessusDinscriptionAdministrative>,
  ): HttpResponse<IProcessusDinscriptionAdministrative> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestProcessusDinscriptionAdministrative[]>,
  ): HttpResponse<IProcessusDinscriptionAdministrative[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
