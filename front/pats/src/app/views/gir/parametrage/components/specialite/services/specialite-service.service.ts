import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SpecialiteModel} from '../models/specialite-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class SpecialiteServiceService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/specialites`;
    
  }

  getSpecialiteList(): Observable<SpecialiteModel[]> {
    return this.http.get<SpecialiteModel[]>(`${this.url}`);
  }

  createSpecialite(specialite: SpecialiteModel): Observable<SpecialiteModel> {
    return this.http.post<any>(`${this.url}`, specialite);
  }

  getSpecialiteById(id:number):Observable<SpecialiteModel>{

    return this.http.get<SpecialiteModel>(`${this.url}/${id}`);
  }

  deleteSpecialite(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateSpecialite(id: number, specialite: SpecialiteModel): Observable<SpecialiteModel> {
    return this.http.put<SpecialiteModel>(`${this.url}/${id}`, specialite);
  }
}
