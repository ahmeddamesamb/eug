import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMention } from 'app/entities/microserviceGIR/mention/mention.model';
import { MentionService } from 'app/entities/microserviceGIR/mention/service/mention.service';
import { ISpecialite } from '../specialite.model';
import { SpecialiteService } from '../service/specialite.service';
import { SpecialiteFormService, SpecialiteFormGroup } from './specialite-form.service';

@Component({
  standalone: true,
  selector: 'jhi-specialite-update',
  templateUrl: './specialite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SpecialiteUpdateComponent implements OnInit {
  isSaving = false;
  specialite: ISpecialite | null = null;

  mentionsSharedCollection: IMention[] = [];

  editForm: SpecialiteFormGroup = this.specialiteFormService.createSpecialiteFormGroup();

  constructor(
    protected specialiteService: SpecialiteService,
    protected specialiteFormService: SpecialiteFormService,
    protected mentionService: MentionService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMention = (o1: IMention | null, o2: IMention | null): boolean => this.mentionService.compareMention(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialite }) => {
      this.specialite = specialite;
      if (specialite) {
        this.updateForm(specialite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specialite = this.specialiteFormService.getSpecialite(this.editForm);
    if (specialite.id !== null) {
      this.subscribeToSaveResponse(this.specialiteService.update(specialite));
    } else {
      this.subscribeToSaveResponse(this.specialiteService.create(specialite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecialite>>): void {
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

  protected updateForm(specialite: ISpecialite): void {
    this.specialite = specialite;
    this.specialiteFormService.resetForm(this.editForm, specialite);

    this.mentionsSharedCollection = this.mentionService.addMentionToCollectionIfMissing<IMention>(
      this.mentionsSharedCollection,
      specialite.mention,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.mentionService
      .query()
      .pipe(map((res: HttpResponse<IMention[]>) => res.body ?? []))
      .pipe(
        map((mentions: IMention[]) => this.mentionService.addMentionToCollectionIfMissing<IMention>(mentions, this.specialite?.mention)),
      )
      .subscribe((mentions: IMention[]) => (this.mentionsSharedCollection = mentions));
  }
}
