import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProgrammationInscription, NewProgrammationInscription } from '../programmation-inscription.model';

export type PartialUpdateProgrammationInscription = Partial<IProgrammationInscription> & Pick<IProgrammationInscription, 'id'>;

type RestOf<T extends IProgrammationInscription | NewProgrammationInscription> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestProgrammationInscription = RestOf<IProgrammationInscription>;

export type NewRestProgrammationInscription = RestOf<NewProgrammationInscription>;

export type PartialUpdateRestProgrammationInscription = RestOf<PartialUpdateProgrammationInscription>;

export type EntityResponseType = HttpResponse<IProgrammationInscription>;
export type EntityArrayResponseType = HttpResponse<IProgrammationInscription[]>;

@Injectable({ providedIn: 'root' })
export class ProgrammationInscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/programmation-inscriptions', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(programmationInscription: NewProgrammationInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programmationInscription);
    return this.http
      .post<RestProgrammationInscription>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(programmationInscription: IProgrammationInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programmationInscription);
    return this.http
      .put<RestProgrammationInscription>(
        `${this.resourceUrl}/${this.getProgrammationInscriptionIdentifier(programmationInscription)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(programmationInscription: PartialUpdateProgrammationInscription): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(programmationInscription);
    return this.http
      .patch<RestProgrammationInscription>(
        `${this.resourceUrl}/${this.getProgrammationInscriptionIdentifier(programmationInscription)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProgrammationInscription>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProgrammationInscription[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProgrammationInscriptionIdentifier(programmationInscription: Pick<IProgrammationInscription, 'id'>): number {
    return programmationInscription.id;
  }

  compareProgrammationInscription(
    o1: Pick<IProgrammationInscription, 'id'> | null,
    o2: Pick<IProgrammationInscription, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getProgrammationInscriptionIdentifier(o1) === this.getProgrammationInscriptionIdentifier(o2) : o1 === o2;
  }

  addProgrammationInscriptionToCollectionIfMissing<Type extends Pick<IProgrammationInscription, 'id'>>(
    programmationInscriptionCollection: Type[],
    ...programmationInscriptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const programmationInscriptions: Type[] = programmationInscriptionsToCheck.filter(isPresent);
    if (programmationInscriptions.length > 0) {
      const programmationInscriptionCollectionIdentifiers = programmationInscriptionCollection.map(
        programmationInscriptionItem => this.getProgrammationInscriptionIdentifier(programmationInscriptionItem)!,
      );
      const programmationInscriptionsToAdd = programmationInscriptions.filter(programmationInscriptionItem => {
        const programmationInscriptionIdentifier = this.getProgrammationInscriptionIdentifier(programmationInscriptionItem);
        if (programmationInscriptionCollectionIdentifiers.includes(programmationInscriptionIdentifier)) {
          return false;
        }
        programmationInscriptionCollectionIdentifiers.push(programmationInscriptionIdentifier);
        return true;
      });
      return [...programmationInscriptionsToAdd, ...programmationInscriptionCollection];
    }
    return programmationInscriptionCollection;
  }

  protected convertDateFromClient<
    T extends IProgrammationInscription | NewProgrammationInscription | PartialUpdateProgrammationInscription,
  >(programmationInscription: T): RestOf<T> {
    return {
      ...programmationInscription,
      dateDebut: programmationInscription.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: programmationInscription.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProgrammationInscription: RestProgrammationInscription): IProgrammationInscription {
    return {
      ...restProgrammationInscription,
      dateDebut: restProgrammationInscription.dateDebut ? dayjs(restProgrammationInscription.dateDebut) : undefined,
      dateFin: restProgrammationInscription.dateFin ? dayjs(restProgrammationInscription.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProgrammationInscription>): HttpResponse<IProgrammationInscription> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProgrammationInscription[]>): HttpResponse<IProgrammationInscription[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
