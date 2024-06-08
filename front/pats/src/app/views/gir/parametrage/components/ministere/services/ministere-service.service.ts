import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {MinistereModel} from '../models/ministere-model'
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class MinistereServiceService {


  private ministereUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.ministereUrl = `${baseUrl}/ministeres`;
    
  }

  getMinistereList(): Observable<MinistereModel[]> {
    return this.http.get<MinistereModel[]>(`${this.ministereUrl}`);
  }


  createMinistere(ministere: any): Observable<MinistereModel> {
    return this.http.post<any>(`${this.ministereUrl}`, ministere);
  }

  getMinistereById(id:number):Observable<MinistereModel>{

    return this.http.get<MinistereModel>(`${this.ministereUrl}/${id}`);
  }

  deleteMinistere(id:number):Observable<void>{

    return this.http.delete<void>(`${this.ministereUrl}/${id}`);
  }

  updateMinistere(id: number, ministere: MinistereModel): Observable<MinistereModel> {
    return this.http.put<MinistereModel>(`${this.ministereUrl}/${id}`, ministere);
}

}
