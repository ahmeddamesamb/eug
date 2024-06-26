import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { IHistoriqueConnexion } from '../historique-connexion.model';
import { HistoriqueConnexionService } from '../service/historique-connexion.service';
import { HistoriqueConnexionFormService, HistoriqueConnexionFormGroup } from './historique-connexion-form.service';

@Component({
  standalone: true,
  selector: 'ugb-historique-connexion-update',
  templateUrl: './historique-connexion-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class HistoriqueConnexionUpdateComponent implements OnInit {
  isSaving = false;
  historiqueConnexion: IHistoriqueConnexion | null = null;

  infosUsersSharedCollection: IInfosUser[] = [];

  editForm: HistoriqueConnexionFormGroup = this.historiqueConnexionFormService.createHistoriqueConnexionFormGroup();

  constructor(
    protected historiqueConnexionService: HistoriqueConnexionService,
    protected historiqueConnexionFormService: HistoriqueConnexionFormService,
    protected infosUserService: InfosUserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInfosUser = (o1: IInfosUser | null, o2: IInfosUser | null): boolean => this.infosUserService.compareInfosUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ historiqueConnexion }) => {
      this.historiqueConnexion = historiqueConnexion;
      if (historiqueConnexion) {
        this.updateForm(historiqueConnexion);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const historiqueConnexion = this.historiqueConnexionFormService.getHistoriqueConnexion(this.editForm);
    if (historiqueConnexion.id !== null) {
      this.subscribeToSaveResponse(this.historiqueConnexionService.update(historiqueConnexion));
    } else {
      this.subscribeToSaveResponse(this.historiqueConnexionService.create(historiqueConnexion));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistoriqueConnexion>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(historiqueConnexion: IHistoriqueConnexion): void {
    this.historiqueConnexion = historiqueConnexion;
    this.historiqueConnexionFormService.resetForm(this.editForm, historiqueConnexion);

    this.infosUsersSharedCollection = this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(
      this.infosUsersSharedCollection,
      historiqueConnexion.infoUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.infosUserService
      .query()
      .pipe(map((res: HttpResponse<IInfosUser[]>) => res.body ?? []))
      .pipe(
        map((infosUsers: IInfosUser[]) =>
          this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(infosUsers, this.historiqueConnexion?.infoUser),
        ),
      )
      .subscribe((infosUsers: IInfosUser[]) => (this.infosUsersSharedCollection = infosUsers));
  }
}
