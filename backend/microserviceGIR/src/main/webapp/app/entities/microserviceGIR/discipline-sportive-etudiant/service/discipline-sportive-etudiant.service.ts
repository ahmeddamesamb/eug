import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDisciplineSportiveEtudiant, NewDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';

export type PartialUpdateDisciplineSportiveEtudiant = Partial<IDisciplineSportiveEtudiant> & Pick<IDisciplineSportiveEtudiant, 'id'>;

export type EntityResponseType = HttpResponse<IDisciplineSportiveEtudiant>;
export type EntityArrayResponseType = HttpResponse<IDisciplineSportiveEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class DisciplineSportiveEtudiantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/discipline-sportive-etudiants', 'microservicegir');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(disciplineSportiveEtudiant: NewDisciplineSportiveEtudiant): Observable<EntityResponseType> {
    return this.http.post<IDisciplineSportiveEtudiant>(this.resourceUrl, disciplineSportiveEtudiant, { observe: 'response' });
  }

  update(disciplineSportiveEtudiant: IDisciplineSportiveEtudiant): Observable<EntityResponseType> {
    return this.http.put<IDisciplineSportiveEtudiant>(
      `${this.resourceUrl}/${this.getDisciplineSportiveEtudiantIdentifier(disciplineSportiveEtudiant)}`,
      disciplineSportiveEtudiant,
      { observe: 'response' },
    );
  }

  partialUpdate(disciplineSportiveEtudiant: PartialUpdateDisciplineSportiveEtudiant): Observable<EntityResponseType> {
    return this.http.patch<IDisciplineSportiveEtudiant>(
      `${this.resourceUrl}/${this.getDisciplineSportiveEtudiantIdentifier(disciplineSportiveEtudiant)}`,
      disciplineSportiveEtudiant,
      { observe: 'response' },
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDisciplineSportiveEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDisciplineSportiveEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getDisciplineSportiveEtudiantIdentifier(disciplineSportiveEtudiant: Pick<IDisciplineSportiveEtudiant, 'id'>): number {
    return disciplineSportiveEtudiant.id;
  }

  compareDisciplineSportiveEtudiant(
    o1: Pick<IDisciplineSportiveEtudiant, 'id'> | null,
    o2: Pick<IDisciplineSportiveEtudiant, 'id'> | null,
  ): boolean {
    return o1 && o2 ? this.getDisciplineSportiveEtudiantIdentifier(o1) === this.getDisciplineSportiveEtudiantIdentifier(o2) : o1 === o2;
  }

  addDisciplineSportiveEtudiantToCollectionIfMissing<Type extends Pick<IDisciplineSportiveEtudiant, 'id'>>(
    disciplineSportiveEtudiantCollection: Type[],
    ...disciplineSportiveEtudiantsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const disciplineSportiveEtudiants: Type[] = disciplineSportiveEtudiantsToCheck.filter(isPresent);
    if (disciplineSportiveEtudiants.length > 0) {
      const disciplineSportiveEtudiantCollectionIdentifiers = disciplineSportiveEtudiantCollection.map(
        disciplineSportiveEtudiantItem => this.getDisciplineSportiveEtudiantIdentifier(disciplineSportiveEtudiantItem)!,
      );
      const disciplineSportiveEtudiantsToAdd = disciplineSportiveEtudiants.filter(disciplineSportiveEtudiantItem => {
        const disciplineSportiveEtudiantIdentifier = this.getDisciplineSportiveEtudiantIdentifier(disciplineSportiveEtudiantItem);
        if (disciplineSportiveEtudiantCollectionIdentifiers.includes(disciplineSportiveEtudiantIdentifier)) {
          return false;
        }
        disciplineSportiveEtudiantCollectionIdentifiers.push(disciplineSportiveEtudiantIdentifier);
        return true;
      });
      return [...disciplineSportiveEtudiantsToAdd, ...disciplineSportiveEtudiantCollection];
    }
    return disciplineSportiveEtudiantCollection;
  }
}
