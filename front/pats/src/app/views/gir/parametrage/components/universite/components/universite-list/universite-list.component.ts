import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {UniversiteService} from '../../services/universite.service'
import {UniversiteModel} from '../../models/universite-model'
import { NumberToStringPipe } from '../../../../../../../pipes/number-to-string.pipe'
import {AlerteComponent} from 'src/app/shared/components/alerte/alerte/alerte.component'



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
} from '@coreui/angular-pro';
import { delay } from 'rxjs';

@Component({
  selector: 'app-universite-list',
  standalone: true,
  imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
    ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
    CardComponent, CardHeaderComponent, ColComponent,ToasterComponent,AlerteComponent
    ],
  templateUrl: './universite-list.component.html',
  styleUrl: './universite-list.component.scss'
})
export class UniversiteListComponent {
  constructor (private route:Router,private universiteService: UniversiteService){

  }

  universiteList : UniversiteModel[] = [];
  itemDelete!: UniversiteModel;
  itemUpdate!: UniversiteModel;
  public liveDemoVisible = false;
  isloading = false;



  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.isloading = true;
    this.universiteService.getUniversiteList().subscribe({
      next: (data) => {
        this.universiteList = data;
        this.isloading = false;
      },
      error: (err) => {


      }
    });
  }

  columns: IColumn[] = [
    {
      key: 'nomUniversite'
    },
    {
      key: 'sigleUniversite'
    },
    {
      key: 'ministere'
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
    this.route.navigate(['/gir/parametrage/universite/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/parametrage/universite/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/universite/update',item])
  }


  delete(){
    this.universiteService.deleteUniversite(this.itemDelete.id).subscribe({
      next: (data) => {
        this.getListe();
        this.liveDemoVisible = false;
        this.addToast(true);
      },
      error: (err) => {
        this.liveDemoVisible = false;
        this.addToast(false);
      }
      
    })
  }

  toggleLiveDemo(item : UniversiteModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }









  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;
  
  addToast(value: boolean) {
    var options = {
      title: `Suppression`,
      texte: `Echec de suppression du ministere`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if(value){
      options.texte = `Suppression du ministere avec succes`;
      options.color = 'success';

    }
    
    
    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value)
    });
    componentRef.instance['visibleChange'].emit(true);

  }
}
