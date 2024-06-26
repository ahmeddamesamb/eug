import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';
import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { UserProfileService } from '../service/user-profile.service';
import { IUserProfile } from '../user-profile.model';
import { UserProfileFormService, UserProfileFormGroup } from './user-profile-form.service';

@Component({
  standalone: true,
  selector: 'ugb-user-profile-update',
  templateUrl: './user-profile-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserProfileUpdateComponent implements OnInit {
  isSaving = false;
  userProfile: IUserProfile | null = null;

  profilsSharedCollection: IProfil[] = [];
  infosUsersSharedCollection: IInfosUser[] = [];

  editForm: UserProfileFormGroup = this.userProfileFormService.createUserProfileFormGroup();

  constructor(
    protected userProfileService: UserProfileService,
    protected userProfileFormService: UserProfileFormService,
    protected profilService: ProfilService,
    protected infosUserService: InfosUserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareProfil = (o1: IProfil | null, o2: IProfil | null): boolean => this.profilService.compareProfil(o1, o2);

  compareInfosUser = (o1: IInfosUser | null, o2: IInfosUser | null): boolean => this.infosUserService.compareInfosUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userProfile }) => {
      this.userProfile = userProfile;
      if (userProfile) {
        this.updateForm(userProfile);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userProfile = this.userProfileFormService.getUserProfile(this.editForm);
    if (userProfile.id !== null) {
      this.subscribeToSaveResponse(this.userProfileService.update(userProfile));
    } else {
      this.subscribeToSaveResponse(this.userProfileService.create(userProfile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserProfile>>): void {
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

  protected updateForm(userProfile: IUserProfile): void {
    this.userProfile = userProfile;
    this.userProfileFormService.resetForm(this.editForm, userProfile);

    this.profilsSharedCollection = this.profilService.addProfilToCollectionIfMissing<IProfil>(
      this.profilsSharedCollection,
      userProfile.profil,
    );
    this.infosUsersSharedCollection = this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(
      this.infosUsersSharedCollection,
      userProfile.infoUser,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.profilService
      .query()
      .pipe(map((res: HttpResponse<IProfil[]>) => res.body ?? []))
      .pipe(map((profils: IProfil[]) => this.profilService.addProfilToCollectionIfMissing<IProfil>(profils, this.userProfile?.profil)))
      .subscribe((profils: IProfil[]) => (this.profilsSharedCollection = profils));

    this.infosUserService
      .query()
      .pipe(map((res: HttpResponse<IInfosUser[]>) => res.body ?? []))
      .pipe(
        map((infosUsers: IInfosUser[]) =>
          this.infosUserService.addInfosUserToCollectionIfMissing<IInfosUser>(infosUsers, this.userProfile?.infoUser),
        ),
      )
      .subscribe((infosUsers: IInfosUser[]) => (this.infosUsersSharedCollection = infosUsers));
  }
}
