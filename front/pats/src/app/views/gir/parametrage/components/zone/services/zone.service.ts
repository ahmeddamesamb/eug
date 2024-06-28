import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CONSTANTES_GLOBALES } from 'src/app/config/constantes.globales';
import { ENVIRONMENT } from 'src/environments/environment';
import {ZoneModel} from '../models/zone-model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ZoneService {

  private zoneUrl;

  constructor(private http: HttpClient) {
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.zoneUrl = `${baseUrl}/zones`;
    
  }
  getZoneList(): Observable<ZoneModel[]> {
    return this.http.get<ZoneModel[]>(`${this.zoneUrl}`);
  }

  createZone(zone: ZoneModel): Observable<ZoneModel> {
    return this.http.post<any>(`${this.zoneUrl}`, zone);
  }

  getZoneById(id:number):Observable<ZoneModel>{

    return this.http.get<ZoneModel>(`${this.zoneUrl}/${id}`);
  }
deleteZone(id:number):Observable<void>{

  return this.http.delete<void>(`${this.zoneUrl}/${id}`);
  }

updateZone(id: number, zone: ZoneModel): Observable<ZoneModel> {
  return this.http.put<ZoneModel>(`${this.zoneUrl}/${id}`, zone);
}

}
