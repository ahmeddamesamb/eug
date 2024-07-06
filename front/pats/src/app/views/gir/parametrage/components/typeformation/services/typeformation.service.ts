import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {TypeformationModel} from '../models/typeformation-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class TypeformationService {

  private typeformationUrl;

  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.typeformationUrl = `${baseUrl}/type-formations`;
  }

  getTypeFormationList(): Observable<TypeformationModel[]> {
    return this.http.get<TypeformationModel[]>(`${this.typeformationUrl}`);
  }

  createTypeformation(typeformation: TypeformationModel): Observable<TypeformationModel> {
    return this.http.post<any>(`${this.typeformationUrl}`, typeformation);
  }

  getTypeformationById(id:number):Observable<TypeformationModel>{

    return this.http.get<TypeformationModel>(`${this.typeformationUrl}/${id}`);
  }

deleteTypeformation(id:number):Observable<void>{

  return this.http.delete<void>(`${this.typeformationUrl}/${id}`);
  }

updateTypeformation(id: number, typeformation: TypeformationModel): Observable<TypeformationModel> {
  return this.http.put<TypeformationModel>(`${this.typeformationUrl}/${id}`, typeformation);
}


}
