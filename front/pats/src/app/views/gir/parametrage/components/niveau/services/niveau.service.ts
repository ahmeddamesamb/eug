import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {NiveauModel} from '../models/niveau-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class NiveauService {

  private niveauUrl;

  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.niveauUrl = `${baseUrl}/niveaus`;
  }

  getNiveauList(): Observable<NiveauModel[]> {
    return this.http.get<NiveauModel[]>(`${this.niveauUrl}`);
  }
  createNiveau(niveau: NiveauModel): Observable<NiveauModel> {
    return this.http.post<any>(`${this.niveauUrl}`, niveau);
  }

  getNiveauById(id:number):Observable<NiveauModel>{

    return this.http.get<NiveauModel>(`${this.niveauUrl}/${id}`);
  }

deleteNiveau(id:number):Observable<void>{

  return this.http.delete<void>(`${this.niveauUrl}/${id}`);
  }

updateNiveau(id: number, niveau: NiveauModel): Observable<NiveauModel> {
  return this.http.put<NiveauModel>(`${this.niveauUrl}/${id}`, niveau);
}
}
