import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IInfosUser } from '../infos-user.model';
import { InfosUserService } from '../service/infos-user.service';
import { InfosUserFormService, InfosUserFormGroup } from './infos-user-form.service';

@Component({
  standalone: true,
  selector: 'ugb-infos-user-update',
  templateUrl: './infos-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InfosUserUpdateComponent implements OnInit {
  isSaving = false;
  infosUser: IInfosUser | null = null;

  usersSharedCollection: IUser[] = [];

  editForm: InfosUserFormGroup = this.infosUserFormService.createInfosUserFormGroup();

  constructor(
    protected infosUserService: InfosUserService,
    protected infosUserFormService: InfosUserFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ infosUser }) => {
      this.infosUser = infosUser;
      if (infosUser) {
        this.updateForm(infosUser);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const infosUser = this.infosUserFormService.getInfosUser(this.editForm);
    if (infosUser.id !== null) {
      this.subscribeToSaveResponse(this.infosUserService.update(infosUser));
    } else {
      this.subscribeToSaveResponse(this.infosUserService.create(infosUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInfosUser>>): void {
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

  protected updateForm(infosUser: IInfosUser): void {
    this.infosUser = infosUser;
    this.infosUserFormService.resetForm(this.editForm, infosUser);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, infosUser.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.infosUser?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
