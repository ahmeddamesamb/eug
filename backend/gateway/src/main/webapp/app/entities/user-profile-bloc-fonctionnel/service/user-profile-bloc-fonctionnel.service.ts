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
import { IUserProfileBlocFonctionnel, NewUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';

export type PartialUpdateUserProfileBlocFonctionnel = Partial<IUserProfileBlocFonctionnel> & Pick<IUserProfileBlocFonctionnel, 'id'>;

type RestOf<T extends IUserProfileBlocFonctionnel | NewUserProfileBlocFonctionnel> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestUserProfileBlocFonctionnel = RestOf<IUserProfileBlocFonctionnel>;

export type NewRestUserProfileBlocFonctionnel = RestOf<NewUserProfileBlocFonctionnel>;

export type PartialUpdateRestUserProfileBlocFonctionnel = RestOf<PartialUpdateUserProfileBlocFonctionnel>;

export type EntityResponseType = HttpResponse<IUserProfileBlocFonctionnel>;
export type EntityArrayResponseType = HttpResponse<IUserProfileBlocFonctionnel[]>;

@Injectable({ providedIn: 'root' })
export class UserProfileBlocFonctionnelService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-profile-bloc-fonctionnels');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/user-profile-bloc-fonctionnels/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(userProfileBlocFonctionnel: NewUserProfileBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProfileBlocFonctionnel);
    return this.http
      .post<RestUserProfileBlocFonctionnel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(userProfileBlocFonctionnel: IUserProfileBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProfileBlocFonctionnel);
    return this.http
      .put<RestUserProfileBlocFonctionnel>(
        `${this.resourceUrl}/${this.getUserProfileBlocFonctionnelIdentifier(userProfileBlocFonctionnel)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userProfileBlocFonctionnel: PartialUpdateUserProfileBlocFonctionnel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userProfileBlocFonctionnel);
    return this.http
      .patch<RestUserProfileBlocFonctionnel>(
        `${this.resourceUrl}/${this.getUserProfileBlocFonctionnelIdentifier(userProfileBlocFonctionnel)}`,
        copy,
        { observe: 'response' },
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestUserProfileBlocFonctionnel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestUserProfileBlocFonctionnel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestUserProfileBlocFonctionnel[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IUserProfileBlocFonctionnel[]>()], asapScheduler)),
    );
  }

  getUserProfileBlocFonctionnelIdentifier(userProfileBlocFonctionnel: Pick<IUserProfileBlocFonctionnel, 'id'>): number {
    return userProfileBlocFonctionnel.id;
  }

  compareUserProfileBlocFonctionnel(
    o1: Pick<IUserProfileBlocFonctionnel, 'id'> | null,
    o2: Pick<IUserProfileBlocFonctionnel, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getUserProfileBlocFonctionnelIdentifier(o1) === this.getUserProfileBlocFonctionnelIdentifier(o2) : o1 === o2;
  }

  addUserProfileBlocFonctionnelToCollectionIfMissing<Type extends Pick<IUserProfileBlocFonctionnel, 'id'>>(
    userProfileBlocFonctionnelCollection: Type[],
    ...userProfileBlocFonctionnelsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userProfileBlocFonctionnels: Type[] = userProfileBlocFonctionnelsToCheck.filter(isPresent);
    if (userProfileBlocFonctionnels.length > 0) {
      const userProfileBlocFonctionnelCollectionIdentifiers = userProfileBlocFonctionnelCollection.map(
        userProfileBlocFonctionnelItem => this.getUserProfileBlocFonctionnelIdentifier(userProfileBlocFonctionnelItem)!,
      );
      const userProfileBlocFonctionnelsToAdd = userProfileBlocFonctionnels.filter(userProfileBlocFonctionnelItem => {
        const userProfileBlocFonctionnelIdentifier = this.getUserProfileBlocFonctionnelIdentifier(userProfileBlocFonctionnelItem);
        if (userProfileBlocFonctionnelCollectionIdentifiers.includes(userProfileBlocFonctionnelIdentifier)) {
          return false;
        }
        userProfileBlocFonctionnelCollectionIdentifiers.push(userProfileBlocFonctionnelIdentifier);
        return true;
      });
      return [...userProfileBlocFonctionnelsToAdd, ...userProfileBlocFonctionnelCollection];
    }
    return userProfileBlocFonctionnelCollection;
  }

  protected convertDateFromClient<
    T extends IUserProfileBlocFonctionnel | NewUserProfileBlocFonctionnel | PartialUpdateUserProfileBlocFonctionnel,
  >(userProfileBlocFonctionnel: T): RestOf<T> {
    return {
      ...userProfileBlocFonctionnel,
      date: userProfileBlocFonctionnel.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restUserProfileBlocFonctionnel: RestUserProfileBlocFonctionnel): IUserProfileBlocFonctionnel {
    return {
      ...restUserProfileBlocFonctionnel,
      date: restUserProfileBlocFonctionnel.date ? dayjs(restUserProfileBlocFonctionnel.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestUserProfileBlocFonctionnel>): HttpResponse<IUserProfileBlocFonctionnel> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(
    res: HttpResponse<RestUserProfileBlocFonctionnel[]>,
  ): HttpResponse<IUserProfileBlocFonctionnel[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
