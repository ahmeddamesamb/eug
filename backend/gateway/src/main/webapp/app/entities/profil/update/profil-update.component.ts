import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IProfil } from '../profil.model';
import { ProfilService } from '../service/profil.service';
import { ProfilFormService, ProfilFormGroup } from './profil-form.service';

@Component({
  standalone: true,
  selector: 'ugb-profil-update',
  templateUrl: './profil-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProfilUpdateComponent implements OnInit {
  isSaving = false;
  profil: IProfil | null = null;

  editForm: ProfilFormGroup = this.profilFormService.createProfilFormGroup();

  constructor(
    protected profilService: ProfilService,
    protected profilFormService: ProfilFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ profil }) => {
      this.profil = profil;
      if (profil) {
        this.updateForm(profil);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const profil = this.profilFormService.getProfil(this.editForm);
    if (profil.id !== null) {
      this.subscribeToSaveResponse(this.profilService.update(profil));
    } else {
      this.subscribeToSaveResponse(this.profilService.create(profil));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfil>>): void {
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

  protected updateForm(profil: IProfil): void {
    this.profil = profil;
    this.profilFormService.resetForm(this.editForm, profil);
  }
}
