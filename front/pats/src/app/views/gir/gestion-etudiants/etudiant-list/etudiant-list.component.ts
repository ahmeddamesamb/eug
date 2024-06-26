import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {EtudiantService} from '../services/etudiant.service';
import {EtudiantModel} from '../models/etudiant-model';
import { NumberToStringPipe } from '../../../../pipes/number-to-string.pipe'
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
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
  PopoverModule,
} from '@coreui/angular-pro';

@Component({
  selector: 'app-etudiant-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent,PopoverModule, ColComponent
  ],
  templateUrl: './etudiant-list.component.html',
  styleUrl: './etudiant-list.component.scss'
})
export class EtudiantListComponent {
  constructor (private route:Router,private etudiantService: EtudiantService,private alertService: AlertServiceService){

  }

  etudiantList : EtudiantModel[] = [];
  itemDelete!: EtudiantModel;
  itemUpdate!: EtudiantModel;
  public liveDemoVisible = false;
  isloading = false;

  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.etudiantService.getEtudiantList().subscribe({
      next: (data) => {
        this.etudiantList = data;
        this.isloading = false;
      },
      error: (err) => {
        this.isloading = false;
      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'codeEtu',
      label: 'Code'
    },
    {
      key: 'nomEtu',
      label: 'Nom'
    },
    {
      key: 'prenomEtu',
      label: 'Prenom'
    },
    {
      key: 'emailUGB',
      label: 'Email'
    },
    {
      key: 'ine',
      label: 'INE'
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
    this.route.navigate(['/gir/gestion-etudiant/view',item])

  }

  create() {
    
    this.route.navigate(['/gir/gestion-etudiant/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/gestion-etudiant/update',item])
  }


  delete(){
    if (this.itemDelete && this.itemDelete.id !== undefined) {
      this.etudiantService.deleteEtudiant(this.itemDelete.id).subscribe({
        next: (data) => {
          this.getListe();
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppréssion","Suppréssion de l'étudiant avec succes","success");
        },
        error: (err) => {
          this.liveDemoVisible = false;
          this.alertService.showToast("Suppréssion","Echec de la suppréssion de l'étudiant","danger");
        }
      });
    } else {
      // Handle the case where id is undefined, if needed
      console.error('Item to delete does not have a valid id.');
    }
  }

  toggleLiveDemo(item : EtudiantModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }


}
