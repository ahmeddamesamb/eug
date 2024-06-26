import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IProfil, NewProfil } from '../profil.model';

export type PartialUpdateProfil = Partial<IProfil> & Pick<IProfil, 'id'>;

type RestOf<T extends IProfil | NewProfil> = Omit<T, 'dateAjout'> & {
  dateAjout?: string | null;
};

export type RestProfil = RestOf<IProfil>;

export type NewRestProfil = RestOf<NewProfil>;

export type PartialUpdateRestProfil = RestOf<PartialUpdateProfil>;

export type EntityResponseType = HttpResponse<IProfil>;
export type EntityArrayResponseType = HttpResponse<IProfil[]>;

@Injectable({ providedIn: 'root' })
export class ProfilService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/profils');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/profils/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(profil: NewProfil): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profil);
    return this.http
      .post<RestProfil>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(profil: IProfil): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profil);
    return this.http
      .put<RestProfil>(`${this.resourceUrl}/${this.getProfilIdentifier(profil)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(profil: PartialUpdateProfil): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(profil);
    return this.http
      .patch<RestProfil>(`${this.resourceUrl}/${this.getProfilIdentifier(profil)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProfil>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProfil[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestProfil[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IProfil[]>()], asapScheduler)),
    );
  }

  getProfilIdentifier(profil: Pick<IProfil, 'id'>): number {
    return profil.id;
  }

  compareProfil(o1: Pick<IProfil, 'id'> | null, o2: Pick<IProfil, 'id'> | null): boolean {
    return o1 && o2 ? this.getProfilIdentifier(o1) === this.getProfilIdentifier(o2) : o1 === o2;
  }

  addProfilToCollectionIfMissing<Type extends Pick<IProfil, 'id'>>(
    profilCollection: Type[],
    ...profilsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const profils: Type[] = profilsToCheck.filter(isPresent);
    if (profils.length > 0) {
      const profilCollectionIdentifiers = profilCollection.map(profilItem => this.getProfilIdentifier(profilItem)!);
      const profilsToAdd = profils.filter(profilItem => {
        const profilIdentifier = this.getProfilIdentifier(profilItem);
        if (profilCollectionIdentifiers.includes(profilIdentifier)) {
          return false;
        }
        profilCollectionIdentifiers.push(profilIdentifier);
        return true;
      });
      return [...profilsToAdd, ...profilCollection];
    }
    return profilCollection;
  }

  protected convertDateFromClient<T extends IProfil | NewProfil | PartialUpdateProfil>(profil: T): RestOf<T> {
    return {
      ...profil,
      dateAjout: profil.dateAjout?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restProfil: RestProfil): IProfil {
    return {
      ...restProfil,
      dateAjout: restProfil.dateAjout ? dayjs(restProfil.dateAjout) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProfil>): HttpResponse<IProfil> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestProfil[]>): HttpResponse<IProfil[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
