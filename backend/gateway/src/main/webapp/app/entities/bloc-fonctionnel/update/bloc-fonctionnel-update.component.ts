import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceUser } from 'app/entities/service-user/service-user.model';
import { ServiceUserService } from 'app/entities/service-user/service/service-user.service';
import { IBlocFonctionnel } from '../bloc-fonctionnel.model';
import { BlocFonctionnelService } from '../service/bloc-fonctionnel.service';
import { BlocFonctionnelFormService, BlocFonctionnelFormGroup } from './bloc-fonctionnel-form.service';

@Component({
  standalone: true,
  selector: 'ugb-bloc-fonctionnel-update',
  templateUrl: './bloc-fonctionnel-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BlocFonctionnelUpdateComponent implements OnInit {
  isSaving = false;
  blocFonctionnel: IBlocFonctionnel | null = null;

  serviceUsersSharedCollection: IServiceUser[] = [];

  editForm: BlocFonctionnelFormGroup = this.blocFonctionnelFormService.createBlocFonctionnelFormGroup();

  constructor(
    protected blocFonctionnelService: BlocFonctionnelService,
    protected blocFonctionnelFormService: BlocFonctionnelFormService,
    protected serviceUserService: ServiceUserService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareServiceUser = (o1: IServiceUser | null, o2: IServiceUser | null): boolean => this.serviceUserService.compareServiceUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ blocFonctionnel }) => {
      this.blocFonctionnel = blocFonctionnel;
      if (blocFonctionnel) {
        this.updateForm(blocFonctionnel);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const blocFonctionnel = this.blocFonctionnelFormService.getBlocFonctionnel(this.editForm);
    if (blocFonctionnel.id !== null) {
      this.subscribeToSaveResponse(this.blocFonctionnelService.update(blocFonctionnel));
    } else {
      this.subscribeToSaveResponse(this.blocFonctionnelService.create(blocFonctionnel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBlocFonctionnel>>): void {
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

  protected updateForm(blocFonctionnel: IBlocFonctionnel): void {
    this.blocFonctionnel = blocFonctionnel;
    this.blocFonctionnelFormService.resetForm(this.editForm, blocFonctionnel);

    this.serviceUsersSharedCollection = this.serviceUserService.addServiceUserToCollectionIfMissing<IServiceUser>(
      this.serviceUsersSharedCollection,
      blocFonctionnel.service,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.serviceUserService
      .query()
      .pipe(map((res: HttpResponse<IServiceUser[]>) => res.body ?? []))
      .pipe(
        map((serviceUsers: IServiceUser[]) =>
          this.serviceUserService.addServiceUserToCollectionIfMissing<IServiceUser>(serviceUsers, this.blocFonctionnel?.service),
        ),
      )
      .subscribe((serviceUsers: IServiceUser[]) => (this.serviceUsersSharedCollection = serviceUsers));
  }
}
