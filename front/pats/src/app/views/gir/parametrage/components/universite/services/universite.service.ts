import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {UniversiteModel} from '../models/universite-model'
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class UniversiteService {

  private universiteUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.universiteUrl = `${baseUrl}/universites`;
    
  }

  getUniversiteList(): Observable<UniversiteModel[]> {
    return this.http.get<UniversiteModel[]>(`${this.universiteUrl}`);
  }


  createUniversite(universite: UniversiteModel): Observable<UniversiteModel> {
    return this.http.post<any>(`${this.universiteUrl}`, universite);
  }

  getUniversiteById(id:number):Observable<UniversiteModel>{

    return this.http.get<UniversiteModel>(`${this.universiteUrl}/${id}`);
  }

  deleteUniversite(id:number):Observable<void>{

    return this.http.delete<void>(`${this.universiteUrl}/${id}`);
  }

  updateUniversite(id: number, universite: UniversiteModel): Observable<UniversiteModel> {
    return this.http.put<UniversiteModel>(`${this.universiteUrl}/${id}`, universite);
  }
}
