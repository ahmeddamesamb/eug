import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDocumentDelivre, NewDocumentDelivre } from '../document-delivre.model';

export type PartialUpdateDocumentDelivre = Partial<IDocumentDelivre> & Pick<IDocumentDelivre, 'id'>;

type RestOf<T extends IDocumentDelivre | NewDocumentDelivre> = Omit<T, 'anneeDoc' | 'dateEnregistrement'> & {
  anneeDoc?: string | null;
  dateEnregistrement?: string | null;
};

export type RestDocumentDelivre = RestOf<IDocumentDelivre>;

export type NewRestDocumentDelivre = RestOf<NewDocumentDelivre>;

export type PartialUpdateRestDocumentDelivre = RestOf<PartialUpdateDocumentDelivre>;

export type EntityResponseType = HttpResponse<IDocumentDelivre>;
export type EntityArrayResponseType = HttpResponse<IDocumentDelivre[]>;

@Injectable({ providedIn: 'root' })
export class DocumentDelivreService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/document-delivres', 'microserviceged');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(documentDelivre: NewDocumentDelivre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDelivre);
    return this.http
      .post<RestDocumentDelivre>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(documentDelivre: IDocumentDelivre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDelivre);
    return this.http
      .put<RestDocumentDelivre>(`${this.resourceUrl}/${this.getDocumentDelivreIdentifier(documentDelivre)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(documentDelivre: PartialUpdateDocumentDelivre): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(documentDelivre);
    return this.http
      .patch<RestDocumentDelivre>(`${this.resourceUrl}/${this.getDocumentDelivreIdentifier(documentDelivre)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDocumentDelivre>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDocumentDelivre[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDocumentDelivreIdentifier(documentDelivre: Pick<IDocumentDelivre, 'id'>): number {
    return documentDelivre.id;
  }

  compareDocumentDelivre(o1: Pick<IDocumentDelivre, 'id'> | null, o2: Pick<IDocumentDelivre, 'id'> | null): boolean {
    return o1 && o2 ? this.getDocumentDelivreIdentifier(o1) === this.getDocumentDelivreIdentifier(o2) : o1 === o2;
  }

  addDocumentDelivreToCollectionIfMissing<Type extends Pick<IDocumentDelivre, 'id'>>(
    documentDelivreCollection: Type[],
    ...documentDelivresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const documentDelivres: Type[] = documentDelivresToCheck.filter(isPresent);
    if (documentDelivres.length > 0) {
      const documentDelivreCollectionIdentifiers = documentDelivreCollection.map(
        documentDelivreItem => this.getDocumentDelivreIdentifier(documentDelivreItem)!,
      );
      const documentDelivresToAdd = documentDelivres.filter(documentDelivreItem => {
        const documentDelivreIdentifier = this.getDocumentDelivreIdentifier(documentDelivreItem);
        if (documentDelivreCollectionIdentifiers.includes(documentDelivreIdentifier)) {
          return false;
        }
        documentDelivreCollectionIdentifiers.push(documentDelivreIdentifier);
        return true;
      });
      return [...documentDelivresToAdd, ...documentDelivreCollection];
    }
    return documentDelivreCollection;
  }

  protected convertDateFromClient<T extends IDocumentDelivre | NewDocumentDelivre | PartialUpdateDocumentDelivre>(
    documentDelivre: T,
  ): RestOf<T> {
    return {
      ...documentDelivre,
      anneeDoc: documentDelivre.anneeDoc?.toJSON() ?? null,
      dateEnregistrement: documentDelivre.dateEnregistrement?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restDocumentDelivre: RestDocumentDelivre): IDocumentDelivre {
    return {
      ...restDocumentDelivre,
      anneeDoc: restDocumentDelivre.anneeDoc ? dayjs(restDocumentDelivre.anneeDoc) : undefined,
      dateEnregistrement: restDocumentDelivre.dateEnregistrement ? dayjs(restDocumentDelivre.dateEnregistrement) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDocumentDelivre>): HttpResponse<IDocumentDelivre> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDocumentDelivre[]>): HttpResponse<IDocumentDelivre[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
