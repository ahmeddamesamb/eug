import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';
import {FormationModel} from '../models/formation-model';
@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private formationUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.formationUrl = `${baseUrl}/formations`;

    
  }

  getFormationList(): Observable<FormationModel[]> {
    return this.http.get<FormationModel[]>(`${this.formationUrl}`);
  }


  createFormation(FormationModel: FormationModel): Observable<FormationModel> {
    return this.http.post<any>(`${this.formationUrl}`,FormationModel);
  }

  getFormationById(id:number):Observable<FormationModel>{

    return this.http.get<FormationModel>(`${this.formationUrl}/${id}`);
  }

  deleteFormation(id:number):Observable<void>{

    return this.http.delete<void>(`${this.formationUrl}/${id}`);
  }

  updateFormation(id: number, inscription: FormationModel): Observable<FormationModel> {
    return this.http.put<FormationModel>(`${this.formationUrl}/${id}`, inscription);
  }
}
