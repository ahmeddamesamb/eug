import { Component, QueryList, ViewChild, ViewChildren, } from '@angular/core';
import { Router } from '@angular/router';
import {MinistereServiceService} from '../../services/ministere-service.service'
import {MinistereModel} from '../../models/ministere-model'
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



@Component({
    selector: 'app-ministere-list',
    standalone: true,
    templateUrl: './ministere-list.component.html',
    styleUrl: './ministere-list.component.scss',
    imports: [BadgeComponent, ButtonDirective, CollapseDirective, SmartTableComponent, TemplateIdDirective, TextColorDirective, NumberToStringPipe,
        ModalBodyComponent, ModalComponent, ModalFooterComponent, ModalHeaderComponent, ModalTitleDirective, ModalToggleDirective, CardBodyComponent,
        CardComponent, CardHeaderComponent, ColComponent,ToasterComponent,AlerteComponent
        ]
})
export class MinistereListComponent {
  constructor (private route:Router,private ministereService: MinistereServiceService){

  }

  ministereList : MinistereModel[] = [];
  itemDelete!: MinistereModel;
  itemUpdate!: MinistereModel;
  public liveDemoVisible = false;


  ngOnInit(): void {
    this.getListe();
 
  }

  getListe(){
    this.ministereService.getMinistereList().subscribe((data)=>{
      this.ministereList = data;
 
    })
  }

  columns: IColumn[] = [
    {
      key: 'nomMinistere'
    },
    {
      key: 'sigleMinistere'
    },
    {
      key: 'dateDebut',
      label: 'Date debut',
      _props: { class: 'text-truncate' }
    },
    {
      key: 'dateFin',
      label: 'Date Fin',
      _props: { class: 'text-truncate' }
    },
    { key: 'enCoursYN', _style: { width: '15%' } },
    {
      key: 'show',
      label: 'Action',
      _style: { width: '5%' },
      filter: false,
      sorter: false
    }
  ];
  details_visible = Object.create({});

  getBadge(enCoursYN: number) {
    switch (enCoursYN) {
      case 1:
        return 'success';
      // case 'Inactive':
      //   return 'secondary';
      // case 'Pending':
      //   return 'warning';
      case 0:
        return 'warning';
      default:
        return 'primary';
    }
  }

  view(item: number) {
    this.route.navigate(['/gir/parametrage/ministere/view',item])

  }
  create() {
    
    this.route.navigate(['/gir/parametrage/ministere/create'])

  }

  update(item:number) {
    this.route.navigate(['/gir/parametrage/ministere/update',item])

      

  }
  delete(){
    this.ministereService.deleteMinistere(this.itemDelete.id).subscribe({
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
  deleter(){
    this.ministereService.deleteMinistere(this.itemDelete.id).subscribe((data)=>{
      console.log(data);
      this.getListe();
      this.liveDemoVisible = false;
      this.addToast(true);
      
    })
  }

  

  toggleLiveDemo(item : MinistereModel) {
    this.itemDelete = item;
    this.liveDemoVisible = !this.liveDemoVisible;
  }

  handleLiveDemoChange(event: boolean) {
    this.liveDemoVisible = event;
  }






  //Pour le toaster

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
