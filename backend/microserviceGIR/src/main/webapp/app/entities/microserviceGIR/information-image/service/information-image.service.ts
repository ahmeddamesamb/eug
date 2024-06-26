import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, asapScheduler, scheduled } from 'rxjs';

import { catchError } from 'rxjs/operators';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IInformationImage, NewInformationImage } from '../information-image.model';

export type PartialUpdateInformationImage = Partial<IInformationImage> & Pick<IInformationImage, 'id'>;

export type EntityResponseType = HttpResponse<IInformationImage>;
export type EntityArrayResponseType = HttpResponse<IInformationImage[]>;

@Injectable({ providedIn: 'root' })
export class InformationImageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/information-images', 'microservicegir');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/information-images/_search', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(informationImage: NewInformationImage): Observable<EntityResponseType> {
    return this.http.post<IInformationImage>(this.resourceUrl, informationImage, { observe: 'response' });
  }

  update(informationImage: IInformationImage): Observable<EntityResponseType> {
    return this.http.put<IInformationImage>(
      `${this.resourceUrl}/${this.getInformationImageIdentifier(informationImage)}`,
      informationImage,
      { observe: 'response' },
    );
  }

  partialUpdate(informationImage: PartialUpdateInformationImage): Observable<EntityResponseType> {
    return this.http.patch<IInformationImage>(
      `${this.resourceUrl}/${this.getInformationImageIdentifier(informationImage)}`,
      informationImage,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInformationImage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInformationImage[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IInformationImage[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(catchError(() => scheduled([new HttpResponse<IInformationImage[]>()], asapScheduler)));
  }

  getInformationImageIdentifier(informationImage: Pick<IInformationImage, 'id'>): number {
    return informationImage.id;
  }

  compareInformationImage(o1: Pick<IInformationImage, 'id'> | null, o2: Pick<IInformationImage, 'id'> | null): boolean {
    return o1 && o2 ? this.getInformationImageIdentifier(o1) === this.getInformationImageIdentifier(o2) : o1 === o2;
  }

  addInformationImageToCollectionIfMissing<Type extends Pick<IInformationImage, 'id'>>(
    informationImageCollection: Type[],
    ...informationImagesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const informationImages: Type[] = informationImagesToCheck.filter(isPresent);
    if (informationImages.length > 0) {
      const informationImageCollectionIdentifiers = informationImageCollection.map(
        informationImageItem => this.getInformationImageIdentifier(informationImageItem)!,
      );
      const informationImagesToAdd = informationImages.filter(informationImageItem => {
        const informationImageIdentifier = this.getInformationImageIdentifier(informationImageItem);
        if (informationImageCollectionIdentifiers.includes(informationImageIdentifier)) {
          return false;
        }
        informationImageCollectionIdentifiers.push(informationImageIdentifier);
        return true;
      });
      return [...informationImagesToAdd, ...informationImageCollection];
    }
    return informationImageCollection;
  }
}
