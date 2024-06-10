import { Component } from '@angular/core';
import {DataDisplayComponent} from "../../../../../../../shared/components/data-display/data-display.component";
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { UniversiteService } from '../../services/universite.service';
import {UniversiteModel} from '../../models/universite-model'
import { Observable,map } from 'rxjs';

import {LoaderComponent} from '../../../../../../../shared/components/loader/loader.component';
import {LoaderService} from '../../../../../../../services/loader/loader.service';

@Component({
  selector: 'app-view',
  standalone: true,
  imports: [DataDisplayComponent, LoaderComponent],
  templateUrl: './view.component.html',
  styleUrl: './view.component.scss'
})
export class ViewComponent {
  id: string ="";
  loadState : boolean = false;


  constructor(private activeRoute: ActivatedRoute, private universiteService: UniversiteService, private loaderservice:LoaderService){

  }

  universite? :UniversiteModel ;

  ngOnInit(): void {
    this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));
    this.loadState  = false;
    id.subscribe((id)=>{
      this.universiteService.getUniversiteById(parseInt(id)).subscribe((data)=>{
        this.universite = data;

        this.loadState = false

        // this.loaderservice.hide();
        // this.loaderservice.loaderState.subscribe((data) =>{
        //   this.loadState = data;
        // })
      })
    })


  }

}
