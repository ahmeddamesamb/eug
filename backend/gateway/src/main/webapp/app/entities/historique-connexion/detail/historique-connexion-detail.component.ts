import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IHistoriqueConnexion } from '../historique-connexion.model';

@Component({
  standalone: true,
  selector: 'ugb-historique-connexion-detail',
  templateUrl: './historique-connexion-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class HistoriqueConnexionDetailComponent {
  @Input() historiqueConnexion: IHistoriqueConnexion | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
