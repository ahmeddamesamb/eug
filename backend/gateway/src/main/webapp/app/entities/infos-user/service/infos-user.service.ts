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
import { IInfosUser, NewInfosUser } from '../infos-user.model';

export type PartialUpdateInfosUser = Partial<IInfosUser> & Pick<IInfosUser, 'id'>;

type RestOf<T extends IInfosUser | NewInfosUser> = Omit<T, 'dateAjout'> & {
  dateAjout?: string | null;
};

export type RestInfosUser = RestOf<IInfosUser>;

export type NewRestInfosUser = RestOf<NewInfosUser>;

export type PartialUpdateRestInfosUser = RestOf<PartialUpdateInfosUser>;

export type EntityResponseType = HttpResponse<IInfosUser>;
export type EntityArrayResponseType = HttpResponse<IInfosUser[]>;

@Injectable({ providedIn: 'root' })
export class InfosUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/infos-users');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/infos-users/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(infosUser: NewInfosUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infosUser);
    return this.http
      .post<RestInfosUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(infosUser: IInfosUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infosUser);
    return this.http
      .put<RestInfosUser>(`${this.resourceUrl}/${this.getInfosUserIdentifier(infosUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(infosUser: PartialUpdateInfosUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(infosUser);
    return this.http
      .patch<RestInfosUser>(`${this.resourceUrl}/${this.getInfosUserIdentifier(infosUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestInfosUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestInfosUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestInfosUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IInfosUser[]>()], asapScheduler)),
    );
  }

  getInfosUserIdentifier(infosUser: Pick<IInfosUser, 'id'>): number {
    return infosUser.id;
  }

  compareInfosUser(o1: Pick<IInfosUser, 'id'> | null, o2: Pick<IInfosUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getInfosUserIdentifier(o1) === this.getInfosUserIdentifier(o2) : o1 === o2;
  }

  addInfosUserToCollectionIfMissing<Type extends Pick<IInfosUser, 'id'>>(
    infosUserCollection: Type[],
    ...infosUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const infosUsers: Type[] = infosUsersToCheck.filter(isPresent);
    if (infosUsers.length > 0) {
      const infosUserCollectionIdentifiers = infosUserCollection.map(infosUserItem => this.getInfosUserIdentifier(infosUserItem)!);
      const infosUsersToAdd = infosUsers.filter(infosUserItem => {
        const infosUserIdentifier = this.getInfosUserIdentifier(infosUserItem);
        if (infosUserCollectionIdentifiers.includes(infosUserIdentifier)) {
          return false;
        }
        infosUserCollectionIdentifiers.push(infosUserIdentifier);
        return true;
      });
      return [...infosUsersToAdd, ...infosUserCollection];
    }
    return infosUserCollection;
  }

  protected convertDateFromClient<T extends IInfosUser | NewInfosUser | PartialUpdateInfosUser>(infosUser: T): RestOf<T> {
    return {
      ...infosUser,
      dateAjout: infosUser.dateAjout?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restInfosUser: RestInfosUser): IInfosUser {
    return {
      ...restInfosUser,
      dateAjout: restInfosUser.dateAjout ? dayjs(restInfosUser.dateAjout) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestInfosUser>): HttpResponse<IInfosUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestInfosUser[]>): HttpResponse<IInfosUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
