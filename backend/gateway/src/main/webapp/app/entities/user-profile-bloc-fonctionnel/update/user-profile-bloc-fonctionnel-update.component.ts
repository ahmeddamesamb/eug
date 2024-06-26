import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { IBlocFonctionnel } from 'app/entities/bloc-fonctionnel/bloc-fonctionnel.model';
import { BlocFonctionnelService } from 'app/entities/bloc-fonctionnel/service/bloc-fonctionnel.service';
import { UserProfileBlocFonctionnelService } from '../service/user-profile-bloc-fonctionnel.service';
import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';
import { UserProfileBlocFonctionnelFormService, UserProfileBlocFonctionnelFormGroup } from './user-profile-bloc-fonctionnel-form.service';

@Component({
  standalone: true,
  selector: 'ugb-user-profile-bloc-fonctionnel-update',
  templateUrl: './user-profile-bloc-fonctionnel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserProfileBlocFonctionnelUpdateComponent implements OnInit {
  isSaving = false;
  userProfileBlocFonctionnel: IUserProfileBlocFonctionnel | null = null;

  userProfilesSharedCollection: IUserProfile[] = [];
  blocFonctionnelsSharedCollection: IBlocFonctionnel[] = [];

  editForm: UserProfileBlocFonctionnelFormGroup = this.userProfileBlocFonctionnelFormService.createUserProfileBlocFonctionnelFormGroup();

  constructor(
    protected userProfileBlocFonctionnelService: UserProfileBlocFonctionnelService,
    protected userProfileBlocFonctionnelFormService: UserProfileBlocFonctionnelFormService,
    protected userProfileService: UserProfileService,
    protected blocFonctionnelService: BlocFonctionnelService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUserProfile = (o1: IUserProfile | null, o2: IUserProfile | null): boolean => this.userProfileService.compareUserProfile(o1, o2);

  compareBlocFonctionnel = (o1: IBlocFonctionnel | null, o2: IBlocFonctionnel | null): boolean =>
    this.blocFonctionnelService.compareBlocFonctionnel(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userProfileBlocFonctionnel }) => {
      this.userProfileBlocFonctionnel = userProfileBlocFonctionnel;
      if (userProfileBlocFonctionnel) {
        this.updateForm(userProfileBlocFonctionnel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userProfileBlocFonctionnel = this.userProfileBlocFonctionnelFormService.getUserProfileBlocFonctionnel(this.editForm);
    if (userProfileBlocFonctionnel.id !== null) {
      this.subscribeToSaveResponse(this.userProfileBlocFonctionnelService.update(userProfileBlocFonctionnel));
    } else {
      this.subscribeToSaveResponse(this.userProfileBlocFonctionnelService.create(userProfileBlocFonctionnel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserProfileBlocFonctionnel>>): void {
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

  protected updateForm(userProfileBlocFonctionnel: IUserProfileBlocFonctionnel): void {
    this.userProfileBlocFonctionnel = userProfileBlocFonctionnel;
    this.userProfileBlocFonctionnelFormService.resetForm(this.editForm, userProfileBlocFonctionnel);

    this.userProfilesSharedCollection = this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(
      this.userProfilesSharedCollection,
      userProfileBlocFonctionnel.userProfil,
    );
    this.blocFonctionnelsSharedCollection = this.blocFonctionnelService.addBlocFonctionnelToCollectionIfMissing<IBlocFonctionnel>(
      this.blocFonctionnelsSharedCollection,
      userProfileBlocFonctionnel.blocFonctionnel,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userProfileService
      .query()
      .pipe(map((res: HttpResponse<IUserProfile[]>) => res.body ?? []))
      .pipe(
        map((userProfiles: IUserProfile[]) =>
          this.userProfileService.addUserProfileToCollectionIfMissing<IUserProfile>(
            userProfiles,
            this.userProfileBlocFonctionnel?.userProfil,
          ),
        ),
      )
      .subscribe((userProfiles: IUserProfile[]) => (this.userProfilesSharedCollection = userProfiles));

    this.blocFonctionnelService
      .query()
      .pipe(map((res: HttpResponse<IBlocFonctionnel[]>) => res.body ?? []))
      .pipe(
        map((blocFonctionnels: IBlocFonctionnel[]) =>
          this.blocFonctionnelService.addBlocFonctionnelToCollectionIfMissing<IBlocFonctionnel>(
            blocFonctionnels,
            this.userProfileBlocFonctionnel?.blocFonctionnel,
          ),
        ),
      )
      .subscribe((blocFonctionnels: IBlocFonctionnel[]) => (this.blocFonctionnelsSharedCollection = blocFonctionnels));
  }
}
