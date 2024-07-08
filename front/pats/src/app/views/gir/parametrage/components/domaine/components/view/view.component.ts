import { Component, ViewChild } from '@angular/core';
import {DataDisplayComponent} from "../../../../../../../shared/components/data-display/data-display.component";
import { NavigationEnd, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { DomaineService } from '../../services/domaine.service';
import {DomaineModel} from '../../models/domaine-model'
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
  listeUfr: string[] = [];


  constructor(private activeRoute: ActivatedRoute,private router: Router, private domaineService: DomaineService, private loaderservice:LoaderService){

  }

  domaine? :DomaineModel ;

  ngOnInit(): void {
    this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));
    this.loadState  = false;
    id.subscribe((id)=>{
      this.domaineService.getDomaineById(parseInt(id)).subscribe((data)=>{
        this.domaine = data;
        this.listeUfr = this.domaine.ufrs?.map(ufr => ufr.sigleUfr!).filter((sigle): sigle is string => !!sigle) || [];

        this.loadState = false
      })
    })


  }

}
