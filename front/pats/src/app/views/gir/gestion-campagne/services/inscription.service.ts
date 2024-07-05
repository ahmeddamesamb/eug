import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {InscriptionModel} from '../components/inscription/models/inscription-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class InscriptionService {

  private inscriptionUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.inscriptionUrl = `${baseUrl}/inscriptions`;
    
  }

  getInscriptionList(): Observable<InscriptionModel[]> {
    return this.http.get<InscriptionModel[]>(`${this.inscriptionUrl}?sort=enCoursYN,desc`);
  }


  createInscription(ministere: InscriptionModel): Observable<InscriptionModel> {
    return this.http.post<any>(`${this.inscriptionUrl}`, ministere);
  }

  getInscriptionById(id:number):Observable<InscriptionModel>{

    return this.http.get<InscriptionModel>(`${this.inscriptionUrl}/${id}`);
  }

  deleteInscription(id:number):Observable<void>{

    return this.http.delete<void>(`${this.inscriptionUrl}/${id}`);
  }

  updateInscription(id: number, inscription: InscriptionModel): Observable<InscriptionModel> {
    return this.http.put<InscriptionModel>(`${this.inscriptionUrl}/${id}`, inscription);
  }
}
