import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { MinistereServiceService } from '../../services/ministere-service.service';
import { MinistereModel } from '../../models/ministere-model';
import { Router, ActivatedRoute } from '@angular/router';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';
import { map, Observable } from 'rxjs';

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1, ToasterComponent],
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {
  ministere: MinistereModel = {
    id: 0,
    nomMinistere: '',
    sigleMinistere: '',
    dateDebut: '',
    dateFin: '',
    enCoursYN: 0,
  };
  ministereForm: FormGroup;
  customStylesValidated = false;
  id: string = "";

  constructor(private ministereService: MinistereServiceService, private route: ActivatedRoute, private router: Router) {
    this.ministereForm = new FormGroup({
      nomMinistere: new FormControl('', Validators.required),
      sigleMinistere: new FormControl('', Validators.required),
      dateDebut: new FormControl(null, Validators.required),
      dateFin: new FormControl(null),
      enCoursYN: new FormControl(0)
    });
  }

  ngOnInit() {
    this.id = this.route.snapshot.params['id'];
    const id: Observable<string> = this.route.params.pipe(map(p=>p['id']));

    console.log('ID récupéré :', this.id);


    id.subscribe((id)=>{
      this.ministereService.getMinistereById(parseInt(id)).subscribe(
        (data) => {
          this.ministere = data;
          console.log(this.ministere);
          this.id = id;
          this.initializeForm(data);
        },
        (err) => {
          console.log(err);
        }
      );
    })
  }

  initializeForm(ministere: MinistereModel) {
    this.ministereForm.setValue({
      nomMinistere: ministere.nomMinistere || '',
      sigleMinistere: ministere.sigleMinistere || '',
      dateDebut: ministere.dateDebut || null,
      dateFin: ministere.dateFin || null,
      enCoursYN: ministere.enCoursYN || 0
    });
  }

  onSubmit1() {
    this.customStylesValidated = true;
    if (this.ministereForm.valid) {
      const enCoursYN = this.ministereForm.value.enCoursYN ? 1 : 0;

      this.ministere = { ...this.ministereForm.value, id: Number(this.id), enCoursYN };      // Convertir la valeur booléenne en entier

      console.log("MISE A JOUR:", this.ministere);
      console.log("ID MINISTERE: ", this.id);
      this.ministereService.updateMinistere(Number(this.id), this.ministere).subscribe({
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.router.navigate(['/gir/parametrage/ministere/view', data.id]);
        },
        error: (err) => {
          console.log(err);
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.ministereForm.value);
    } else {
      console.log('Formulaire invalide');
    }
  }

  annuler(){
    this.router.navigate(['/gir/parametrage/ministere/attente']);
  }

/*   onReset1() {
    this.ministereForm.reset({
      nomMinistere: '',
      sigleMinistere: '',
      dateDebut: null,
      dateFin: null,
      enCoursYN: 0
    });
  } */

  // Pour le toaster
  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.TopEnd;

  addToast(value: boolean) {
    var options = {
      title: `Mise à jour`,
      texte: `Échec de la mise à jour du ministère`,
      delay: 5000,
      placement: this.placement,
      color: 'danger',
      autohide: true,
    };
    if (value) {
      options.texte = `Mise à jour du ministère avec succès`;
      options.color = 'success';
    }

    const componentRef = this.toaster.addToast(AlerteComponent, options, {});
    componentRef.instance['visibleChange'].subscribe((value: any) => {
      console.log('onVisibleChange', value);
    });
    componentRef.instance['visibleChange'].emit(true);
  }
}
