import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {environment} from '../../../../../../../environments/environment.development'
import {MinistereModel} from '../models/ministere-model'

@Injectable({
  providedIn: 'root'
})
export class MinistereServiceService {


  private ministereUrl;

  constructor(private http: HttpClient) {
    const baseUrl = environment.apiURL;
    this.ministereUrl = `${baseUrl}/ministeres`;
  }

  getMinistereList(): Observable<MinistereModel[]> {
    return this.http.get<MinistereModel[]>(`${this.ministereUrl}`);
  }


  createMinistere(ministere: any): Observable<MinistereModel> {
    return this.http.post<any>(`${this.ministereUrl}`, ministere);
  }

  getMinistereById(id:number):Observable<MinistereModel>{

    return this.http.get<MinistereModel>(`${this.ministereUrl}/${id}`);
  }

  deleteMinistere(id:number):Observable<void>{

    return this.http.delete<void>(`${this.ministereUrl}/${id}`);
  }
}
