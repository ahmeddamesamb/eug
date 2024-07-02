import { Component } from '@angular/core';
import {CycleService} from '../../services/cycle.service';
import {CycleModel} from '../../models/cycle-model';
import {FraisModel} from '../../../frais/models/frais-model';
import {FraisService} from '../../../frais/services/frais.service'
import { ActivatedRoute, Router } from '@angular/router';
import { AlertServiceService } from 'src/app/shared/services/alert/alert-service.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-associer-frais',
  standalone: true,
  imports: [],
  templateUrl: './associer-frais.component.html',
  styleUrl: './associer-frais.component.scss'
})
export class AssocierFraisComponent {
 

}
