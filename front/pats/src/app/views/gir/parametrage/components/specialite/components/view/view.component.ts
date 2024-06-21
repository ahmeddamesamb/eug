import { Component, ViewChild } from '@angular/core';
import {DataDisplayComponent} from "../../../../../../../shared/components/data-display/data-display.component";
import { NavigationEnd, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { SpecialiteServiceService } from '../../services/specialite-service.service';
import {SpecialiteModel} from '../../models/specialite-model'
import { Observable,filter,map } from 'rxjs';

import {LoaderComponent} from '../../../../../../../shared/components/loader/loader.component';
import {LoaderService} from '../../../../../../../services/loader/loader.service';
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'

@Component({
  selector: 'app-view',
  standalone: true,
  imports: [DataDisplayComponent, LoaderComponent,NumberToStringPipe],
  templateUrl: './view.component.html',
  styleUrl: './view.component.scss'
})
export class ViewComponent {

  id: string ="";
  loadState : boolean = false;
  previousUrl: string | null = null;

  constructor(private activeRoute: ActivatedRoute,private router: Router, private specialiteService: SpecialiteServiceService, private loaderservice:LoaderService){

  }
  specialite? :SpecialiteModel ;

  ngOnInit(): void {
    this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));
    this.loadState  = false;
    id.subscribe((id)=>{
      this.specialiteService.getSpecialiteById(parseInt(id)).subscribe((data)=>{
        this.specialite = data;

        this.loadState = false
      })
    })


  }

}
