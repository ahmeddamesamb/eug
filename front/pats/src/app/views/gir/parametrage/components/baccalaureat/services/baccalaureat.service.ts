import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {BaccalaureatModel} from '../models/baccalaureat-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class BaccalaureatService {

  private url;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.url = `${baseUrl}/baccalaureats`;
    
  }

  getBaccalaureatList(): Observable<BaccalaureatModel[]> {
    return this.http.get<BaccalaureatModel[]>(`${this.url}`);
  }

  createBaccalaureat(baccalaureat: BaccalaureatModel): Observable<BaccalaureatModel> {
    return this.http.post<any>(`${this.url}`, baccalaureat);
  }

  getBaccalaureatById(id:number):Observable<BaccalaureatModel>{

    return this.http.get<BaccalaureatModel>(`${this.url}/${id}`);
  }

  deleteBaccalaureat(id:number):Observable<void>{

    return this.http.delete<void>(`${this.url}/${id}`);
  }
  updateBaccalaureat(id: number, baccalaureat: BaccalaureatModel): Observable<BaccalaureatModel> {
    return this.http.put<BaccalaureatModel>(`${this.url}/${id}`, baccalaureat);
  }
}
