import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';
import { CampagneModel } from '../../campagnes/models/campagne-model';

@Injectable({
  providedIn: 'root'
})
export class CampagneService {

  private campagneUrl ;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.campagneUrl = `${baseUrl}/campagnes`;
    
  }

  public getCampagneList(): Observable<CampagneModel[]> {
    return this.http.get<CampagneModel[]>(`${this.campagneUrl}`);
  }


  createCampagne(campagne: CampagneModel): Observable<CampagneModel> {
    return this.http.post<any>(`${this.campagneUrl}`, campagne);
  }

  getCampagneById(id:number):Observable<CampagneModel>{

    return this.http.get<CampagneModel>(`${this.campagneUrl}/${id}`);
  }

  deleteCampagne(id:number):Observable<void>{

    return this.http.delete<void>(`${this.campagneUrl}/${id}`);
  }

  updateCampagne(id: number, campagne: CampagneModel): Observable<CampagneModel> {
    return this.http.put<CampagneModel>(`${this.campagneUrl}/${id}`, campagne);
  }
}
