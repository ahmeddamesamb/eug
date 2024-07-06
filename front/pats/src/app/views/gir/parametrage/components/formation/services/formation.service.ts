import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
<<<<<<< HEAD
import { Observable } from 'rxjs';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';
import {FormationModel} from '../models/formation-model';
=======
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {FormationModel} from '../models/formation-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

>>>>>>> eb1fff07f315284ee072e4d730b46db6ad81917c
@Injectable({
  providedIn: 'root'
})
export class FormationService {
  private formationUrl;

<<<<<<< HEAD
  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.formationUrl = `${baseUrl}/formations`;

    
=======
  private formationUrl;

  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.formationUrl = `${baseUrl}/formations`;
>>>>>>> eb1fff07f315284ee072e4d730b46db6ad81917c
  }

  getFormationList(): Observable<FormationModel[]> {
    return this.http.get<FormationModel[]>(`${this.formationUrl}`);
  }

<<<<<<< HEAD

  createFormation(FormationModel: FormationModel): Observable<FormationModel> {
    return this.http.post<any>(`${this.formationUrl}`,FormationModel);
  }

=======
  createFormation(formation: FormationModel): Observable<FormationModel> {
    return this.http.post<any>(`${this.formationUrl}`, formation);
  }
>>>>>>> eb1fff07f315284ee072e4d730b46db6ad81917c
  getFormationById(id:number):Observable<FormationModel>{

    return this.http.get<FormationModel>(`${this.formationUrl}/${id}`);
  }

<<<<<<< HEAD
  deleteFormation(id:number):Observable<void>{

    return this.http.delete<void>(`${this.formationUrl}/${id}`);
  }

  updateFormation(id: number, inscription: FormationModel): Observable<FormationModel> {
    return this.http.put<FormationModel>(`${this.formationUrl}/${id}`, inscription);
  }
=======
deleteFormation(id:number):Observable<void>{

  return this.http.delete<void>(`${this.formationUrl}/${id}`);
  }

updateFormation(id: number, formation: FormationModel): Observable<FormationModel> {
  return this.http.put<FormationModel>(`${this.formationUrl}/${id}`, formation);
}

>>>>>>> eb1fff07f315284ee072e4d730b46db6ad81917c
}
