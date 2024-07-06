import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {InformationPersonellesModel} from '../models/information-personelles-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class InformationPersonnellesService {
  

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/information-personnelles`; 
  }

  getInformationPersonnelleList(): Observable<InformationPersonellesModel[]> {
    return this.http.get<InformationPersonellesModel[]>(`${this.url}`);
  }

  createInformationPersonnelle(infos: InformationPersonellesModel): Observable<InformationPersonellesModel> {
    return this.http.post<any>(`${this.url}`, infos);
  }

  getInformationPersonnelleById(id:number):Observable<InformationPersonellesModel>{

    return this.http.get<InformationPersonellesModel>(`${this.url}/${id}`);
  }

  deleteInformationPersonnelle(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateInformationPersonnelle(id: number, infos: InformationPersonellesModel): Observable<InformationPersonellesModel> {
    return this.http.put<InformationPersonellesModel>(`${this.url}/${id}`, infos);
  }



  getEtudiantCode(code:string):Observable<InformationPersonellesModel>{

    return this.http.get<InformationPersonellesModel>(`${this.url}/etudiant/${code}`);
  }
}
