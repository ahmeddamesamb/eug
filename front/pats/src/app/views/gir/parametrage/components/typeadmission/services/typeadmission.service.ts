import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TypeadmissionModel} from '../models/typeadmission-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class TypeadmissionService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/type-admissions`;
    
  }

  getTypeAdmissionList(): Observable<TypeadmissionModel[]> {
    return this.http.get<TypeadmissionModel[]>(`${this.url}`);
  }

  createTypeAdmission(infoPerso: TypeadmissionModel): Observable<TypeadmissionModel> {
    return this.http.post<any>(`${this.url}`, infoPerso);
  }

  getTypeAdmissionById(id:number):Observable<TypeadmissionModel>{

    return this.http.get<TypeadmissionModel>(`${this.url}/${id}`);
  }

  deleteTypeAdmissionById(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateTypeAdmission(id: number, infoPerso: TypeadmissionModel): Observable<TypeadmissionModel> {
    return this.http.put<TypeadmissionModel>(`${this.url}/${id}`, infoPerso);
  }
}
