import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IServiceUser } from '../service-user.model';
import { ServiceUserService } from '../service/service-user.service';
import { ServiceUserFormService, ServiceUserFormGroup } from './service-user-form.service';

@Component({
  standalone: true,
  selector: 'ugb-service-user-update',
  templateUrl: './service-user-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceUserUpdateComponent implements OnInit {
  isSaving = false;
  serviceUser: IServiceUser | null = null;

  editForm: ServiceUserFormGroup = this.serviceUserFormService.createServiceUserFormGroup();

  constructor(
    protected serviceUserService: ServiceUserService,
    protected serviceUserFormService: ServiceUserFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceUser }) => {
      this.serviceUser = serviceUser;
      if (serviceUser) {
        this.updateForm(serviceUser);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const serviceUser = this.serviceUserFormService.getServiceUser(this.editForm);
    if (serviceUser.id !== null) {
      this.subscribeToSaveResponse(this.serviceUserService.update(serviceUser));
    } else {
      this.subscribeToSaveResponse(this.serviceUserService.create(serviceUser));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceUser>>): void {
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

  protected updateForm(serviceUser: IServiceUser): void {
    this.serviceUser = serviceUser;
    this.serviceUserFormService.resetForm(this.editForm, serviceUser);
  }
}
