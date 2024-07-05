import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {DepartementModel} from '../models/departement-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class DepartementService {

  private departementUrl;

  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.departementUrl = `${baseUrl}/departements`;
  }
  getDepartementList(): Observable<DepartementModel[]> {
    return this.http.get<DepartementModel[]>(`${this.departementUrl}`);
  }

  createDepartement(departement: DepartementModel): Observable<DepartementModel> {
    return this.http.post<any>(`${this.departementUrl}`, departement);
  }

  getDepartementById(id:number):Observable<DepartementModel>{

    return this.http.get<DepartementModel>(`${this.departementUrl}/${id}`);
  }

deleteDepartement(id:number):Observable<void>{

  return this.http.delete<void>(`${this.departementUrl}/${id}`);
  }

updateDepartement(id: number, departement: DepartementModel): Observable<DepartementModel> {
  return this.http.put<DepartementModel>(`${this.departementUrl}/${id}`, departement);
}

}
