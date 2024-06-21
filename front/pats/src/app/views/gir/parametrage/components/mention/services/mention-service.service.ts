import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {DomaineModel} from '../../domaine/models/domaine-model';
import {MentionModel} from '../models/mention-model';
import { ENVIRONMENT} from '../../../../../../../environments/environment';
import {CONSTANTES_GLOBALES} from '../../../../../../config/constantes.globales';


@Injectable({
  providedIn: 'root'
})
export class MentionServiceService {
  private mentionUrl;

  constructor(private http: HttpClient) { 
    const baseUrl = ENVIRONMENT.endpointURL + CONSTANTES_GLOBALES.girURL;
    this.mentionUrl = `${baseUrl}/mentions`;
  }

  getMentionList(): Observable<MentionModel[]> {
    return this.http.get<MentionModel[]>(`${this.mentionUrl}`);
  }

  createMention(mention: MentionModel): Observable<MentionModel> {
    return this.http.post<any>(`${this.mentionUrl}`, mention);
  }

  getMentionById(id:number):Observable<MentionModel>{

    return this.http.get<MentionModel>(`${this.mentionUrl}/${id}`);
  }

deleteMention(id:number):Observable<void>{

  return this.http.delete<void>(`${this.mentionUrl}/${id}`);
  }

updateMention(id: number, mention: MentionModel): Observable<MentionModel> {
  return this.http.put<MentionModel>(`${this.mentionUrl}/${id}`, mention);
}
}
