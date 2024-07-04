import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TypeselectionModel} from '../models/typeselection-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class TypeselectionService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/type-selections`;
    
  }

  getTypeSelectionList(): Observable<TypeselectionModel[]> {
    return this.http.get<TypeselectionModel[]>(`${this.url}`);
  }

  createTypeSelection(typeSelection: TypeselectionModel): Observable<TypeselectionModel> {
    return this.http.post<any>(`${this.url}`, typeSelection);
  }

  getTypeSelectionById(id:number):Observable<TypeselectionModel>{

    return this.http.get<TypeselectionModel>(`${this.url}/${id}`);
  }

  deleteTypeSelection(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateTypeSelection(id: number, typeSelection: TypeselectionModel): Observable<TypeselectionModel> {
    return this.http.put<TypeselectionModel>(`${this.url}/${id}`, typeSelection);
  }
}
