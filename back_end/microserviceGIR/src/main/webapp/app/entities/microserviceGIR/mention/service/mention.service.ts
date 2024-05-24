import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMention, NewMention } from '../mention.model';

export type PartialUpdateMention = Partial<IMention> & Pick<IMention, 'id'>;

export type EntityResponseType = HttpResponse<IMention>;
export type EntityArrayResponseType = HttpResponse<IMention[]>;

@Injectable({ providedIn: 'root' })
export class MentionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/mentions', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(mention: NewMention): Observable<EntityResponseType> {
    return this.http.post<IMention>(this.resourceUrl, mention, { observe: 'response' });
  }

  update(mention: IMention): Observable<EntityResponseType> {
    return this.http.put<IMention>(`${this.resourceUrl}/${this.getMentionIdentifier(mention)}`, mention, { observe: 'response' });
  }

  partialUpdate(mention: PartialUpdateMention): Observable<EntityResponseType> {
    return this.http.patch<IMention>(`${this.resourceUrl}/${this.getMentionIdentifier(mention)}`, mention, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMention>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMention[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMentionIdentifier(mention: Pick<IMention, 'id'>): number {
    return mention.id;
  }

  compareMention(o1: Pick<IMention, 'id'> | null, o2: Pick<IMention, 'id'> | null): boolean {
    return o1 && o2 ? this.getMentionIdentifier(o1) === this.getMentionIdentifier(o2) : o1 === o2;
  }

  addMentionToCollectionIfMissing<Type extends Pick<IMention, 'id'>>(
    mentionCollection: Type[],
    ...mentionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const mentions: Type[] = mentionsToCheck.filter(isPresent);
    if (mentions.length > 0) {
      const mentionCollectionIdentifiers = mentionCollection.map(mentionItem => this.getMentionIdentifier(mentionItem)!);
      const mentionsToAdd = mentions.filter(mentionItem => {
        const mentionIdentifier = this.getMentionIdentifier(mentionItem);
        if (mentionCollectionIdentifiers.includes(mentionIdentifier)) {
          return false;
        }
        mentionCollectionIdentifiers.push(mentionIdentifier);
        return true;
      });
      return [...mentionsToAdd, ...mentionCollection];
    }
    return mentionCollection;
  }
}
