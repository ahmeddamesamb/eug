import { Component, ViewChild } from '@angular/core';
import {DataDisplayComponent} from "../../../../../../../shared/components/data-display/data-display.component";
import { NavigationEnd, Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { MentionServiceService } from '../../services/mention-service.service';
import { MentionModel } from '../../models/mention-model';
import { Observable,filter,map } from 'rxjs';

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
  previousUrl: string | null = null;


  constructor(private activeRoute: ActivatedRoute,private router: Router, private mentionService: MentionServiceService, private loaderservice:LoaderService){
    this.previousUrl = '';

    const previousUrl = this.router.getCurrentNavigation();
    console.log('URL précédente :', previousUrl);
  }

  mention? :MentionModel ;

  ngOnInit(): void {
    this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));
    this.loadState  = false;
    id.subscribe((id)=>{
      this.mentionService.getMentionById(parseInt(id)).subscribe((data)=>{
        this.mention = data;

        this.loadState = false
      })
    })


  }

}
