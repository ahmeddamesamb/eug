import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {DomaineModel} from '../models/domaine-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class DomaineService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/domaines`;
    
  }

  getDomaineList(): Observable<DomaineModel[]> {
    return this.http.get<DomaineModel[]>(`${this.url}`);
  }

  createDomaine(ufr: DomaineModel): Observable<DomaineModel> {
    return this.http.post<any>(`${this.url}`, ufr);
  }

  getDomaineById(id:number):Observable<DomaineModel>{

    return this.http.get<DomaineModel>(`${this.url}/${id}`);
  }

  deleteDomaine(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateDomaine(id: number, ufr: DomaineModel): Observable<DomaineModel> {
    return this.http.put<DomaineModel>(`${this.url}/${id}`, ufr);
  }
}
