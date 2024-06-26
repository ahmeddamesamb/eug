import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {UfrModel} from '../models/ufr-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class UfrServiceService {
  private ufrUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.ufrUrl = `${baseUrl}/ufrs`;
    
  }

  getUfrList(): Observable<UfrModel[]> {
    return this.http.get<UfrModel[]>(`${this.ufrUrl}`);
  }

  createUfr(ufr: UfrModel): Observable<UfrModel> {
    return this.http.post<any>(`${this.ufrUrl}`, ufr);
  }

  getUfrById(id:number):Observable<UfrModel>{

    return this.http.get<UfrModel>(`${this.ufrUrl}/${id}`);
  }

deleteUfr(id:number):Observable<void>{

  return this.http.delete<void>(`${this.ufrUrl}/${id}`);
  }

updateUfr(id: number, ufr: UfrModel): Observable<UfrModel> {
  return this.http.put<UfrModel>(`${this.ufrUrl}/${id}`, ufr);
}

}
