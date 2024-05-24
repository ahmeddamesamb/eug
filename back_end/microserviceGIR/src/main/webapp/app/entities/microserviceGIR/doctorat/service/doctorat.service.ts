import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDoctorat, NewDoctorat } from '../doctorat.model';

export type PartialUpdateDoctorat = Partial<IDoctorat> & Pick<IDoctorat, 'id'>;

type RestOf<T extends IDoctorat | NewDoctorat> = Omit<T, 'anneeInscriptionDoctorat'> & {
  anneeInscriptionDoctorat?: string | null;
};

export type RestDoctorat = RestOf<IDoctorat>;

export type NewRestDoctorat = RestOf<NewDoctorat>;

export type PartialUpdateRestDoctorat = RestOf<PartialUpdateDoctorat>;

export type EntityResponseType = HttpResponse<IDoctorat>;
export type EntityArrayResponseType = HttpResponse<IDoctorat[]>;

@Injectable({ providedIn: 'root' })
export class DoctoratService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/doctorats', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(doctorat: NewDoctorat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorat);
    return this.http
      .post<RestDoctorat>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(doctorat: IDoctorat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorat);
    return this.http
      .put<RestDoctorat>(`${this.resourceUrl}/${this.getDoctoratIdentifier(doctorat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(doctorat: PartialUpdateDoctorat): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(doctorat);
    return this.http
      .patch<RestDoctorat>(`${this.resourceUrl}/${this.getDoctoratIdentifier(doctorat)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestDoctorat>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestDoctorat[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDoctoratIdentifier(doctorat: Pick<IDoctorat, 'id'>): number {
    return doctorat.id;
  }

  compareDoctorat(o1: Pick<IDoctorat, 'id'> | null, o2: Pick<IDoctorat, 'id'> | null): boolean {
    return o1 && o2 ? this.getDoctoratIdentifier(o1) === this.getDoctoratIdentifier(o2) : o1 === o2;
  }

  addDoctoratToCollectionIfMissing<Type extends Pick<IDoctorat, 'id'>>(
    doctoratCollection: Type[],
    ...doctoratsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const doctorats: Type[] = doctoratsToCheck.filter(isPresent);
    if (doctorats.length > 0) {
      const doctoratCollectionIdentifiers = doctoratCollection.map(doctoratItem => this.getDoctoratIdentifier(doctoratItem)!);
      const doctoratsToAdd = doctorats.filter(doctoratItem => {
        const doctoratIdentifier = this.getDoctoratIdentifier(doctoratItem);
        if (doctoratCollectionIdentifiers.includes(doctoratIdentifier)) {
          return false;
        }
        doctoratCollectionIdentifiers.push(doctoratIdentifier);
        return true;
      });
      return [...doctoratsToAdd, ...doctoratCollection];
    }
    return doctoratCollection;
  }

  protected convertDateFromClient<T extends IDoctorat | NewDoctorat | PartialUpdateDoctorat>(doctorat: T): RestOf<T> {
    return {
      ...doctorat,
      anneeInscriptionDoctorat: doctorat.anneeInscriptionDoctorat?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restDoctorat: RestDoctorat): IDoctorat {
    return {
      ...restDoctorat,
      anneeInscriptionDoctorat: restDoctorat.anneeInscriptionDoctorat ? dayjs(restDoctorat.anneeInscriptionDoctorat) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestDoctorat>): HttpResponse<IDoctorat> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestDoctorat[]>): HttpResponse<IDoctorat[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
