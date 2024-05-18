import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormationValide, NewFormationValide } from '../formation-valide.model';

export type PartialUpdateFormationValide = Partial<IFormationValide> & Pick<IFormationValide, 'id'>;

type RestOf<T extends IFormationValide | NewFormationValide> = Omit<T, 'dateDebut' | 'dateFin'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
};

export type RestFormationValide = RestOf<IFormationValide>;

export type NewRestFormationValide = RestOf<NewFormationValide>;

export type PartialUpdateRestFormationValide = RestOf<PartialUpdateFormationValide>;

export type EntityResponseType = HttpResponse<IFormationValide>;
export type EntityArrayResponseType = HttpResponse<IFormationValide[]>;

@Injectable({ providedIn: 'root' })
export class FormationValideService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-valides', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(formationValide: NewFormationValide): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationValide);
    return this.http
      .post<RestFormationValide>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(formationValide: IFormationValide): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationValide);
    return this.http
      .put<RestFormationValide>(`${this.resourceUrl}/${this.getFormationValideIdentifier(formationValide)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(formationValide: PartialUpdateFormationValide): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationValide);
    return this.http
      .patch<RestFormationValide>(`${this.resourceUrl}/${this.getFormationValideIdentifier(formationValide)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFormationValide>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFormationValide[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFormationValideIdentifier(formationValide: Pick<IFormationValide, 'id'>): number {
    return formationValide.id;
  }

  compareFormationValide(o1: Pick<IFormationValide, 'id'> | null, o2: Pick<IFormationValide, 'id'> | null): boolean {
    return o1 && o2 ? this.getFormationValideIdentifier(o1) === this.getFormationValideIdentifier(o2) : o1 === o2;
  }

  addFormationValideToCollectionIfMissing<Type extends Pick<IFormationValide, 'id'>>(
    formationValideCollection: Type[],
    ...formationValidesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const formationValides: Type[] = formationValidesToCheck.filter(isPresent);
    if (formationValides.length > 0) {
      const formationValideCollectionIdentifiers = formationValideCollection.map(
        formationValideItem => this.getFormationValideIdentifier(formationValideItem)!,
      );
      const formationValidesToAdd = formationValides.filter(formationValideItem => {
        const formationValideIdentifier = this.getFormationValideIdentifier(formationValideItem);
        if (formationValideCollectionIdentifiers.includes(formationValideIdentifier)) {
          return false;
        }
        formationValideCollectionIdentifiers.push(formationValideIdentifier);
        return true;
      });
      return [...formationValidesToAdd, ...formationValideCollection];
    }
    return formationValideCollection;
  }

  protected convertDateFromClient<T extends IFormationValide | NewFormationValide | PartialUpdateFormationValide>(
    formationValide: T,
  ): RestOf<T> {
    return {
      ...formationValide,
      dateDebut: formationValide.dateDebut?.format(DATE_FORMAT) ?? null,
      dateFin: formationValide.dateFin?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restFormationValide: RestFormationValide): IFormationValide {
    return {
      ...restFormationValide,
      dateDebut: restFormationValide.dateDebut ? dayjs(restFormationValide.dateDebut) : undefined,
      dateFin: restFormationValide.dateFin ? dayjs(restFormationValide.dateFin) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFormationValide>): HttpResponse<IFormationValide> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFormationValide[]>): HttpResponse<IFormationValide[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
