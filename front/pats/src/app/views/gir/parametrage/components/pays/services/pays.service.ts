import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CONSTANTES_GLOBALES } from 'src/app/config/constantes.globales';
import { ENVIRONMENT } from 'src/environments/environment';
import {PaysModel} from '../models/pays-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaysService {

  private paysUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.paysUrl = `${baseUrl}/pays`;
    
  }

  getPaysList(): Observable<PaysModel[]> {
    return this.http.get<PaysModel[]>(`${this.paysUrl}`);
  }

  createPays(pays: PaysModel): Observable<PaysModel> {
    return this.http.post<any>(`${this.paysUrl}`, pays);
  }

  getPaysById(id:number):Observable<PaysModel>{

    return this.http.get<PaysModel>(`${this.paysUrl}/${id}`);
  }

deletePays(id:number):Observable<void>{

  return this.http.delete<void>(`${this.paysUrl}/${id}`);
  }

updatePays(id: number, pays: PaysModel): Observable<PaysModel> {
  return this.http.put<PaysModel>(`${this.paysUrl}/${id}`, pays);
}
}
