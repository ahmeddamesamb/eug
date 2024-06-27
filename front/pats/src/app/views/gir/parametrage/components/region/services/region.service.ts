import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CONSTANTES_GLOBALES } from 'src/app/config/constantes.globales';
import { ENVIRONMENT } from 'src/environments/environment';
import {RegionModel} from '../models/region-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegionService {

  private regionUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.regionUrl = `${baseUrl}/regions`;
    
  }
  getRegionList(): Observable<RegionModel[]> {
    return this.http.get<RegionModel[]>(`${this.regionUrl}`);
  }

  createRegion(region: RegionModel): Observable<RegionModel> {
    return this.http.post<any>(`${this.regionUrl}`, region);
  }

  getRegionById(id:number):Observable<RegionModel>{

    return this.http.get<RegionModel>(`${this.regionUrl}/${id}`);
  }

deleteRegion(id:number):Observable<void>{

  return this.http.delete<void>(`${this.regionUrl}/${id}`);
  }

updateRegion(id: number, region: RegionModel): Observable<RegionModel> {
  return this.http.put<RegionModel>(`${this.regionUrl}/${id}`, region);
}
}
