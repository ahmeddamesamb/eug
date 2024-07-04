import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TypeHandicapModel} from '../models/type-handicap-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class TypeHandicapService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/type-handicaps`;
    
  }

  getTypeHandicapList(): Observable<TypeHandicapModel[]> {
    return this.http.get<TypeHandicapModel[]>(`${this.url}`);
  }

  createTypeHandicap(typeHandicap: TypeHandicapModel): Observable<TypeHandicapModel> {
    return this.http.post<any>(`${this.url}`, typeHandicap);
  }

  getTypeHandicapById(id:number):Observable<TypeHandicapModel>{

    return this.http.get<TypeHandicapModel>(`${this.url}/${id}`);
  }

  deleteTypeHandicap(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateTypeHandicap(id: number, typeHandicap: TypeHandicapModel): Observable<TypeHandicapModel> {
    return this.http.put<TypeHandicapModel>(`${this.url}/${id}`, typeHandicap);
  }
}
