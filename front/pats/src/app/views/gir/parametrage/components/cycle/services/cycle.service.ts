import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {CycleModel} from '../models/cycle-model'
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';

@Injectable({
  providedIn: 'root'
})
export class CycleService {

  private cycleUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.cycleUrl = `${baseUrl}/cycles`;
    
  }
  getCycleList(): Observable<CycleModel[]> {
    return this.http.get<CycleModel[]>(`${this.cycleUrl}`);
  }


  createCycle(cycle: CycleModel): Observable<CycleModel> {
    return this.http.post<any>(`${this.cycleUrl}`, cycle);
  }

  getCycleById(id:number):Observable<CycleModel>{

    return this.http.get<CycleModel>(`${this.cycleUrl}/${id}`);
  }

  deleteCycle(id:number):Observable<void>{

    return this.http.delete<void>(`${this.cycleUrl}/${id}`);
  }

  updateCycle(id: number, cycle: CycleModel): Observable<CycleModel> {
    return this.http.put<CycleModel>(`${this.cycleUrl}/${id}`, cycle);
  }

}
