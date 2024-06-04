import { Component } from '@angular/core';
import {DataDisplayComponent} from "../../../../../../../shared/components/data-display/data-display.component";
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { MinistereServiceService } from '../../services/ministere-service.service';
import {MinistereModel} from '../../models/ministere-model'
import { Observable,map } from 'rxjs';

@Component({
  selector: 'app-view',
  standalone: true,
  imports: [DataDisplayComponent],
  templateUrl: './view.component.html',
  styleUrl: './view.component.scss'
})
export class ViewComponent {
  
  id: string ="";
  constructor(private activeRoute: ActivatedRoute, private ministereService: MinistereServiceService){

  }
  ministere? :MinistereModel ;
  ngOnInit(): void {
    this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));

    id.subscribe((id)=>{
      this.ministereService.getMinistereById(parseInt(id)).subscribe((data)=>{
        this.ministere = data;
      })
    })
    
    
  }





}
