import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {EtudiantModel} from '../models/etudiant-model';
import { ENVIRONMENT} from '../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class EtudiantService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/etudiants`;
    
  }

  getEtudiantList(): Observable<EtudiantModel[]> {
    return this.http.get<EtudiantModel[]>(`${this.url}`);
  }

  createEtudiant(etudiant: EtudiantModel): Observable<EtudiantModel> {
    return this.http.post<any>(`${this.url}`, etudiant);
  }

  getEtudiantById(id:number):Observable<EtudiantModel>{

    return this.http.get<EtudiantModel>(`${this.url}/${id}`);
  }

  deleteEtudiant(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateEtudiant(id: number, domaine: EtudiantModel): Observable<EtudiantModel> {
    return this.http.put<EtudiantModel>(`${this.url}/${id}`, domaine);
  }
}
