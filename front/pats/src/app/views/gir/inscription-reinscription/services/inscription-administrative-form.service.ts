import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {InscriptionAdministrativeFormModel} from '../models/inscription-administrative-form-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class InscriptionAdministrativeFormService {

  private iafUrl;


  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.iafUrl = `${baseUrl}/inscription-administrative-formations`;
  }

  getIafList(): Observable<InscriptionAdministrativeFormModel[]> {
    return this.http.get<InscriptionAdministrativeFormModel[]>(`${this.iafUrl}`);
  }
  createIaf(iaf: InscriptionAdministrativeFormModel): Observable<InscriptionAdministrativeFormModel> {
    return this.http.post<any>(`${this.iafUrl}`, iaf);
  }
  getIafById(id:number):Observable<InscriptionAdministrativeFormModel>{

    return this.http.get<InscriptionAdministrativeFormModel>(`${this.iafUrl}/${id}`);
  }

deleteIaf(id:number):Observable<void>{

  return this.http.delete<void>(`${this.iafUrl}/${id}`);
  }

updateIaf(id: number, iaf: InscriptionAdministrativeFormModel): Observable<InscriptionAdministrativeFormModel> {
  return this.http.put<InscriptionAdministrativeFormModel>(`${this.iafUrl}/${id}`, iaf);
}

}
