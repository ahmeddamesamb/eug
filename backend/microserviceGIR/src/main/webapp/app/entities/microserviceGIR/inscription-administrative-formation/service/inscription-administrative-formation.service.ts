import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInscriptionAdministrativeFormation, NewInscriptionAdministrativeFormation } from '../inscription-administrative-formation.model';

export type PartialUpdateInscriptionAdministrativeFormation = Partial<IInscriptionAdministrativeFormation> &
  Pick<IInscriptionAdministrativeFormation, 'id'>;

type RestOf<T extends IInscriptionAdministrativeFormation | NewInscriptionAdministrativeFormation> = Omit<
  T,
  'dateChoixFormation' | 'dateValidationInscription'
> & {
  dateChoixFormation?: string | null;
  dateValidationInscription?: string | null;
};

export type RestInscriptionAdministrativeFormation = RestOf<IInscriptionAdministrativeFormation>;

export type NewRestInscriptionAdministrativeFormation = RestOf<NewInscriptionAdministrativeFormation>;

export type PartialUpdateRestInscriptionAdministrativeFormation = RestOf<PartialUpdateInscriptionAdministrativeFormation>;

export type EntityResponseType = HttpResponse<IInscriptionAdministrativeFormation>;
export type EntityArrayResponseType = HttpResponse<IInscriptionAdministrativeFormation[]>;

@Injectable({ providedIn: 'root' })
export class InscriptionAdministrativeFormationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/inscription-administrative-formations', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor(
    'api/inscription-administrative-formations/_search',
    'microservicegir',
  );

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(inscriptionAdministrativeFormation: NewInscriptionAdministrativeFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionAdministrativeFormation);
    return this.http
      .post<RestInscriptionAdministrativeFormation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(inscriptionAdministrativeFormation: IInscriptionAdministrativeFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionAdministrativeFormation);
    return this.http
      .put<RestInscriptionAdministrativeFormation>(
        `${this.resourceUrl}/${this.getInscriptionAdministrativeFormationIdentifier(inscriptionAdministrativeFormation)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(inscriptionAdministrativeFormation: PartialUpdateInscriptionAdministrativeFormation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(inscriptionAdministrativeFormation);
    return this.http
      .patch<RestInscriptionAdministrativeFormation>(
        `${this.resourceUrl}/${this.getInscriptionAdministrativeFormationIdentifier(inscriptionAdministrativeFormation)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInscriptionAdministrativeFormation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInscriptionAdministrativeFormation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestInscriptionAdministrativeFormation[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IInscriptionAdministrativeFormation[]>()], asapScheduler)),
    );
  }

  getInscriptionAdministrativeFormationIdentifier(
    inscriptionAdministrativeFormation: Pick<IInscriptionAdministrativeFormation, 'id'>,
  ): number {
    return inscriptionAdministrativeFormation.id;
  }

  compareInscriptionAdministrativeFormation(
    o1: Pick<IInscriptionAdministrativeFormation, 'id'> | null,
    o2: Pick<IInscriptionAdministrativeFormation, 'id'> | null,
  ): boolean {
    return o1 && o2
      ? this.getInscriptionAdministrativeFormationIdentifier(o1) === this.getInscriptionAdministrativeFormationIdentifier(o2)
      : o1 === o2;
  }

  addInscriptionAdministrativeFormationToCollectionIfMissing<Type extends Pick<IInscriptionAdministrativeFormation, 'id'>>(
    inscriptionAdministrativeFormationCollection: Type[],
    ...inscriptionAdministrativeFormationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const inscriptionAdministrativeFormations: Type[] = inscriptionAdministrativeFormationsToCheck.filter(isPresent);
    if (inscriptionAdministrativeFormations.length > 0) {
      const inscriptionAdministrativeFormationCollectionIdentifiers = inscriptionAdministrativeFormationCollection.map(
        inscriptionAdministrativeFormationItem =>
          this.getInscriptionAdministrativeFormationIdentifier(inscriptionAdministrativeFormationItem)!,
      );
      const inscriptionAdministrativeFormationsToAdd = inscriptionAdministrativeFormations.filter(
        inscriptionAdministrativeFormationItem => {
          const inscriptionAdministrativeFormationIdentifier = this.getInscriptionAdministrativeFormationIdentifier(
            inscriptionAdministrativeFormationItem,
          );
          if (inscriptionAdministrativeFormationCollectionIdentifiers.includes(inscriptionAdministrativeFormationIdentifier)) {
            return false;
          }
          inscriptionAdministrativeFormationCollectionIdentifiers.push(inscriptionAdministrativeFormationIdentifier);
          return true;
        },
      );
      return [...inscriptionAdministrativeFormationsToAdd, ...inscriptionAdministrativeFormationCollection];
    }
    return inscriptionAdministrativeFormationCollection;
  }

  protected convertDateFromClient<
    T extends IInscriptionAdministrativeFormation | NewInscriptionAdministrativeFormation | PartialUpdateInscriptionAdministrativeFormation,
  >(inscriptionAdministrativeFormation: T): RestOf<T> {
    return {
      ...inscriptionAdministrativeFormation,
      dateChoixFormation: inscriptionAdministrativeFormation.dateChoixFormation?.toJSON() ?? null,
      dateValidationInscription: inscriptionAdministrativeFormation.dateValidationInscription?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(
    restInscriptionAdministrativeFormation: RestInscriptionAdministrativeFormation,
  ): IInscriptionAdministrativeFormation {
    return {
      ...restInscriptionAdministrativeFormation,
      dateChoixFormation: restInscriptionAdministrativeFormation.dateChoixFormation
        ? dayjs(restInscriptionAdministrativeFormation.dateChoixFormation)
        : undefined,
      dateValidationInscription: restInscriptionAdministrativeFormation.dateValidationInscription
        ? dayjs(restInscriptionAdministrativeFormation.dateValidationInscription)
        : undefined,
    };
  }

  protected convertResponseFromServer(
    res: HttpResponse<RestInscriptionAdministrativeFormation>,
  ): HttpResponse<IInscriptionAdministrativeFormation> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestInscriptionAdministrativeFormation[]>,
  ): HttpResponse<IInscriptionAdministrativeFormation[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
