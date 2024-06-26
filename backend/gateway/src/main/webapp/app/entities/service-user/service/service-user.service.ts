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
import { IServiceUser, NewServiceUser } from '../service-user.model';

export type PartialUpdateServiceUser = Partial<IServiceUser> & Pick<IServiceUser, 'id'>;

type RestOf<T extends IServiceUser | NewServiceUser> = Omit<T, 'dateAjout'> & {
  dateAjout?: string | null;
};

export type RestServiceUser = RestOf<IServiceUser>;

export type NewRestServiceUser = RestOf<NewServiceUser>;

export type PartialUpdateRestServiceUser = RestOf<PartialUpdateServiceUser>;

export type EntityResponseType = HttpResponse<IServiceUser>;
export type EntityArrayResponseType = HttpResponse<IServiceUser[]>;

@Injectable({ providedIn: 'root' })
export class ServiceUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-users');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/service-users/_search');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(serviceUser: NewServiceUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceUser);
    return this.http
      .post<RestServiceUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(serviceUser: IServiceUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceUser);
    return this.http
      .put<RestServiceUser>(`${this.resourceUrl}/${this.getServiceUserIdentifier(serviceUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(serviceUser: PartialUpdateServiceUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(serviceUser);
    return this.http
      .patch<RestServiceUser>(`${this.resourceUrl}/${this.getServiceUserIdentifier(serviceUser)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestServiceUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestServiceUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<RestServiceUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' }).pipe(
      map(res => this.convertResponseArrayFromServer(res)),
      catchError(() => scheduled([new HttpResponse<IServiceUser[]>()], asapScheduler)),
    );
  }

  getServiceUserIdentifier(serviceUser: Pick<IServiceUser, 'id'>): number {
    return serviceUser.id;
  }

  compareServiceUser(o1: Pick<IServiceUser, 'id'> | null, o2: Pick<IServiceUser, 'id'> | null): boolean {
    return o1 && o2 ? this.getServiceUserIdentifier(o1) === this.getServiceUserIdentifier(o2) : o1 === o2;
  }

  addServiceUserToCollectionIfMissing<Type extends Pick<IServiceUser, 'id'>>(
    serviceUserCollection: Type[],
    ...serviceUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const serviceUsers: Type[] = serviceUsersToCheck.filter(isPresent);
    if (serviceUsers.length > 0) {
      const serviceUserCollectionIdentifiers = serviceUserCollection.map(
        serviceUserItem => this.getServiceUserIdentifier(serviceUserItem)!,
      );
      const serviceUsersToAdd = serviceUsers.filter(serviceUserItem => {
        const serviceUserIdentifier = this.getServiceUserIdentifier(serviceUserItem);
        if (serviceUserCollectionIdentifiers.includes(serviceUserIdentifier)) {
          return false;
        }
        serviceUserCollectionIdentifiers.push(serviceUserIdentifier);
        return true;
      });
      return [...serviceUsersToAdd, ...serviceUserCollection];
    }
    return serviceUserCollection;
  }

  protected convertDateFromClient<T extends IServiceUser | NewServiceUser | PartialUpdateServiceUser>(serviceUser: T): RestOf<T> {
    return {
      ...serviceUser,
      dateAjout: serviceUser.dateAjout?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restServiceUser: RestServiceUser): IServiceUser {
    return {
      ...restServiceUser,
      dateAjout: restServiceUser.dateAjout ? dayjs(restServiceUser.dateAjout) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestServiceUser>): HttpResponse<IServiceUser> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestServiceUser[]>): HttpResponse<IServiceUser[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
