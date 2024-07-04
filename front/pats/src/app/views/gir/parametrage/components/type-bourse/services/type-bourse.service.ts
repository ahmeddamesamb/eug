import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TypeBourseModel} from '../models/type-bourse-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class TypeBourseService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/type-bourses`;
    
  }

  getTypeBourseList(): Observable<TypeBourseModel[]> {
    return this.http.get<TypeBourseModel[]>(`${this.url}`);
  }

  createTypeBourse(typeBourse: TypeBourseModel): Observable<TypeBourseModel> {
    return this.http.post<any>(`${this.url}`, typeBourse);
  }

  getTypeBourseById(id:number):Observable<TypeBourseModel>{

    return this.http.get<TypeBourseModel>(`${this.url}/${id}`);
  }

  deleteTypeBourse(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateTypeBourse(id: number, typeBourse: TypeBourseModel): Observable<TypeBourseModel> {
    return this.http.put<TypeBourseModel>(`${this.url}/${id}`, typeBourse);
  }
}
