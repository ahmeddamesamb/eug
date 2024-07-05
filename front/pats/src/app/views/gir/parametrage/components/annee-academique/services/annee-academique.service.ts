import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';
import {AnneeAcademiqueModel} from '../models/AnneeAcademiqueModel';
@Injectable({
  providedIn: 'root'
})
export class AnneeAcademiqueService {

  private anneeAcacemiqueUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.anneeAcacemiqueUrl = `${baseUrl}/annee-academiques`;

    
  }

  getAnneeAcademiqueList(): Observable<AnneeAcademiqueModel[]> {
    return this.http.get<AnneeAcademiqueModel[]>(`${this.anneeAcacemiqueUrl}`);
  }


  createAnneeAcademique(inscription: AnneeAcademiqueModel): Observable<AnneeAcademiqueModel> {
    return this.http.post<any>(`${this.anneeAcacemiqueUrl}`,inscription);
  }

  getAnneeAcademiqueById(id:number):Observable<AnneeAcademiqueModel>{

    return this.http.get<AnneeAcademiqueModel>(`${this.anneeAcacemiqueUrl}/${id}`);
  }

  deleteAcademique(id:number):Observable<void>{

    return this.http.delete<void>(`${this.anneeAcacemiqueUrl}/${id}`);
  }

  updateAcademique(id: number, inscription: AnneeAcademiqueModel): Observable<AnneeAcademiqueModel> {
    return this.http.put<AnneeAcademiqueModel>(`${this.anneeAcacemiqueUrl}/${id}`, inscription);
  }
}
