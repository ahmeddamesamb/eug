import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {MentionServiceService} from '../../services/mention-service.service';
import {MentionModel} from '../../models/mention-model';
import {AlertServiceService} from 'src/app/shared/services/alert/alert-service.service'

import{
  BadgeComponent,
  ButtonDirective,
  CollapseDirective,
  IColumn,
  SmartTableComponent,
  TemplateIdDirective,
  TextColorDirective,
  ModalBodyComponent,
  ModalComponent,
  ModalFooterComponent,
  ModalHeaderComponent,
  ModalTitleDirective,
  ModalToggleDirective,
  CardBodyComponent,
  CardComponent,
  CardHeaderComponent,
  ColComponent,
  ToasterPlacement,
  ToasterComponent,
  PopoverModule,
} from '@coreui/angular-pro';
import { delay } from 'rxjs';

@Component({
  selector: 'app-mention-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent,ToasterComponent
    ],
  templateUrl: './mention-list.component.html',
  styleUrl: './mention-list.component.scss'
})
export class MentionListComponent {

  constructor (private route:Router,private mentionService: MentionServiceService,private alertService: AlertServiceService){

  }

  mentionList : MentionModel[] = [];
  itemDelete!: MentionModel;
  itemUpdate!: MentionModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }
  getListe(){
    this.isloading = true;
    this.mentionService.getMentionList().subscribe({
      next: (data) => {
        this.mentionList = data;
        this.isloading = false;
      },
      error: (err) => {


      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'libelleMention',
      label: 'Nom'
    },
    
    {
      key: 'domaine',
      label: 'Domaine'
    },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];

  view(item: number) {
    this.route.navigate(['/gir/parametrage/mention/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/parametrage/mention/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/mention/update',item])
  }

  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.mentionService.deleteMention(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Suppression d'une mention avec succes","success");

        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppression","Echec de la suppression de la mention","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : MentionModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }


}
