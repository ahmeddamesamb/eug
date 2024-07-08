import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {InscriptionAdministrativeModel} from '../models/inscription-administrative-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class InscriptionAdministrativeService {

  private iaUrl;


  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.iaUrl = `${baseUrl}/inscription-administratives`;
  }

  getIaList(): Observable<InscriptionAdministrativeModel[]> {
    return this.http.get<InscriptionAdministrativeModel[]>(`${this.iaUrl}`);
  }
  createIaf(ia: InscriptionAdministrativeModel): Observable<InscriptionAdministrativeModel> {
    return this.http.post<any>(`${this.iaUrl}`, ia);
  }
  getIaById(id:number):Observable<InscriptionAdministrativeModel>{

    return this.http.get<InscriptionAdministrativeModel>(`${this.iaUrl}/${id}`);
  }

deleteIa(id:number):Observable<void>{

  return this.http.delete<void>(`${this.iaUrl}/${id}`);
  }

updateIa(id: number, ia: InscriptionAdministrativeModel): Observable<InscriptionAdministrativeModel> {
  return this.http.put<InscriptionAdministrativeModel>(`${this.iaUrl}/${id}`, ia);
}
}
