import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {FormationAutoriseModel} from '../models/formation-autorise-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';


@Injectable({
  providedIn: 'root'
})
export class FormationAutoriseService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/formation-autorisees`;
    
  }

  getFormationAutoriseList(): Observable<FormationAutoriseModel[]> {
    return this.http.get<FormationAutoriseModel[]>(`${this.url}`);
  }

  createFormationAutorise(FormationAutorise: FormationAutoriseModel): Observable<FormationAutoriseModel> {
    return this.http.post<any>(`${this.url}`, FormationAutorise);
  }

  getFormationAutoriseById(id:number):Observable<FormationAutoriseModel>{

    return this.http.get<FormationAutoriseModel>(`${this.url}/${id}`);
  }

  deleteFormationAutorise(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateFormationAutorise(id: number, FormationAutorise: FormationAutoriseModel): Observable<FormationAutoriseModel> {
    return this.http.put<FormationAutoriseModel>(`${this.url}/${id}`, FormationAutorise);
  }
}
