import { Component, OnInit, ViewChild } from '@angular/core';
import { ReactiveFormsModule, FormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { DocsExampleComponent } from '@docs-components/public-api';
import { RowComponent, ColComponent, TextColorDirective, CardComponent, DatePickerComponent as DatePickerComponent_1, CardHeaderComponent, CardBodyComponent, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, ToasterComponent, ToasterPlacement } from '@coreui/angular-pro';
import { DatePipe } from '@angular/common';
import { MinistereServiceService } from '../../services/ministere-service.service';
import { MinistereModel } from '../../models/ministere-model';
import { Router, ActivatedRoute } from '@angular/router';
import { AlerteComponent } from 'src/app/shared/components/alerte/alerte/alerte.component';

@Component({
  selector: 'app-update',
  standalone: true,
  imports: [RowComponent, ColComponent, TextColorDirective, CardComponent, CardHeaderComponent, CardBodyComponent, DocsExampleComponent, ReactiveFormsModule, FormsModule, FormDirective, FormLabelDirective, FormControlDirective, FormFeedbackComponent, InputGroupComponent, InputGroupTextDirective, FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, DatePipe, DatePickerComponent_1, ToasterComponent],
  templateUrl: './update.component.html',
  styleUrl: './update.component.scss'
})
export class UpdateComponent implements OnInit {
  ministere?: MinistereModel;
  ministereForm: FormGroup | undefined;
  customStylesValidated = false;

  constructor(private ministereService: MinistereServiceService, private route: ActivatedRoute, private router: Router) {}

  ngOnInit() {
    this.ministereForm = new FormGroup({
      nomMinistere: new FormControl('', Validators.required),
      sigleMinistere: new FormControl('', Validators.required),
      dateDebut: new FormControl(null, Validators.required)
    });

    const id = this.route.snapshot.params['id'];
    this.ministereService.getMinistereById(id).subscribe(
      (data) => {
        this.ministere = data;
        this.ministereForm!.patchValue(data);
      },
      (err) => {
        console.log(err);
      }
    );
  }

  onSubmit1() {
    this.customStylesValidated = true;
    if (this.ministereForm!.valid) {
      this.ministere = this.ministereForm!.value;
      this.ministereService.updateMinistere(this.ministere!.id!, this.ministere!).subscribe({
        next: (data) => {
          console.log(data);
          this.addToast(true);
          this.router.navigate(['/gir/parametrage/ministere/view', data.id]);
        },
        error: (err) => {
          this.addToast(false);
        }
      });
      console.log('Données du formulaire :', this.ministereForm!.value);
    } else {
      console.log('Formulaire invalide');
    }
  }

  onReset1() {
    this.ministereForm!.reset();
  }

  // Pour le toaster
  @ViewChild(ToasterComponent) toaster!: ToasterComponent;
  placement = ToasterPlacement.BottomEnd;

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