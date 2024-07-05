import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {LyceeModel} from '../models/lycee-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class LyceeService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/lycees`;
    
  }

  getLyceeList(): Observable<LyceeModel[]> {
    return this.http.get<LyceeModel[]>(`${this.url}`);
  }

  createLycee(lycee: LyceeModel): Observable<LyceeModel> {
    return this.http.post<any>(`${this.url}`, lycee);
  }

  getLyceeById(id:number):Observable<LyceeModel>{

    return this.http.get<LyceeModel>(`${this.url}/${id}`);
  }

  deleteLycee(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateLycee(id: number, lycee: LyceeModel): Observable<LyceeModel> {
    return this.http.put<LyceeModel>(`${this.url}/${id}`, lycee);
  }
}
