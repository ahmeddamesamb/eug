import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {SerieModel} from '../models/serie-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class SerieService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/series`;
    
  }

  getSerieList(): Observable<SerieModel[]> {
    return this.http.get<SerieModel[]>(`${this.url}`);
  }

  createSerie(serie: SerieModel): Observable<SerieModel> {
    return this.http.post<any>(`${this.url}`, serie);
  }

  getSerieById(id:number):Observable<SerieModel>{

    return this.http.get<SerieModel>(`${this.url}/${id}`);
  }

  deleteSerie(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateSerie(id: number, serie: SerieModel): Observable<SerieModel> {
    return this.http.put<SerieModel>(`${this.url}/${id}`, serie);
  }
}
