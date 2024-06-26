import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { RessourceService } from 'app/entities/ressource/service/ressource.service';
import { InfoUserRessourceService } from '../service/info-user-ressource.service';
import { IInfoUserRessource } from '../info-user-ressource.model';
import { InfoUserRessourceFormService, InfoUserRessourceFormGroup } from './info-user-ressource-form.service';

@Component({
  standalone: true,
  selector: 'ugb-info-user-ressource-update',
  templateUrl: './info-user-ressource-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InfoUserRessourceUpdateComponent implements OnInit {
  isSaving = false;
  infoUserRessource: IInfoUserRessource | null = null;

  infosUsersSharedCollection: IInfosUser[] = [];
  ressourcesSharedCollection: IRessource[] = [];

  editForm: InfoUserRessourceFormGroup = this.infoUserRessourceFormService.createInfoUserRessourceFormGroup();

  constructor(
    protected infoUserRessourceService: InfoUserRessourceService,
    protected infoUserRessourceFormService: InfoUserRessourceFormService,
    protected infosUserService: InfosUserService,
    protected ressourceService: RessourceService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareInfosUser = (o1: IInfosUser | null, o2: IInfosUser | null): boolean => this.infosUserService.compareInfosUser(o1, o2);

  compareRessource = (o1: IRessource | null, o2: IRessource | null): boolean => this.ressourceService.compareRessource(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infoUserRessource }) => {
      this.infoUserRessource = infoUserRessource;
      if (infoUserRessource) {
        this.updateForm(infoUserRessource);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infoUserRessource = this.infoUserRessourceFormService.getInfoUserRessource(this.editForm);
    if (infoUserRessource.id !== null) {
      this.subscribeToSaveResponse(this.infoUserRessourceService.update(infoUserRessource));
    } else {
      this.subscribeToSaveResponse(this.infoUserRessourceService.create(infoUserRessource));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfoUserRessource>>): void {
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

  protected updateForm(infoUserRessource: IInfoUserRessource): void {
    this.infoUserRessource = infoUserRessource;
    this.infoUserRessourceFormService.resetForm(this.editForm, infoUserRessource);

    this.infosUsersSharedCollection = this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(
      this.infosUsersSharedCollection,
      infoUserRessource.infosUser,
    );
    this.ressourcesSharedCollection = this.ressourceService.addRessourceToCollectionIfMissing<IRessource>(
      this.ressourcesSharedCollection,
      infoUserRessource.ressource,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.infosUserService
      .query()
      .pipe(map((res: HttpResponse<IInfosUser[]>) => res.body ?? []))
      .pipe(
        map((infosUsers: IInfosUser[]) =>
          this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(infosUsers, this.infoUserRessource?.infosUser),
        ),
      )
      .subscribe((infosUsers: IInfosUser[]) => (this.infosUsersSharedCollection = infosUsers));

    this.ressourceService
      .query()
      .pipe(map((res: HttpResponse<IRessource[]>) => res.body ?? []))
      .pipe(
        map((ressources: IRessource[]) =>
          this.ressourceService.addRessourceToCollectionIfMissing<IRessource>(ressources, this.infoUserRessource?.ressource),
        ),
      )
      .subscribe((ressources: IRessource[]) => (this.ressourcesSharedCollection = ressources));
  }
}
