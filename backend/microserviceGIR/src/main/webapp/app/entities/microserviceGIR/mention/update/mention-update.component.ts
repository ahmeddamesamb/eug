import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';
import { DomaineService } from 'app/entities/microserviceGIR/domaine/service/domaine.service';
import { IMention } from '../mention.model';
import { MentionService } from '../service/mention.service';
import { MentionFormService, MentionFormGroup } from './mention-form.service';

@Component({
  standalone: true,
  selector: 'jhi-mention-update',
  templateUrl: './mention-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MentionUpdateComponent implements OnInit {
  isSaving = false;
  mention: IMention | null = null;

  domainesSharedCollection: IDomaine[] = [];

  editForm: MentionFormGroup = this.mentionFormService.createMentionFormGroup();

  constructor(
    protected mentionService: MentionService,
    protected mentionFormService: MentionFormService,
    protected domaineService: DomaineService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDomaine = (o1: IDomaine | null, o2: IDomaine | null): boolean => this.domaineService.compareDomaine(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mention }) => {
      this.mention = mention;
      if (mention) {
        this.updateForm(mention);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mention = this.mentionFormService.getMention(this.editForm);
    if (mention.id !== null) {
      this.subscribeToSaveResponse(this.mentionService.update(mention));
    } else {
      this.subscribeToSaveResponse(this.mentionService.create(mention));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMention>>): void {
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

  protected updateForm(mention: IMention): void {
    this.mention = mention;
    this.mentionFormService.resetForm(this.editForm, mention);

    this.domainesSharedCollection = this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(
      this.domainesSharedCollection,
      mention.domaine,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.domaineService
      .query()
      .pipe(map((res: HttpResponse<IDomaine[]>) => res.body ?? []))
      .pipe(map((domaines: IDomaine[]) => this.domaineService.addDomaineToCollectionIfMissing<IDomaine>(domaines, this.mention?.domaine)))
      .subscribe((domaines: IDomaine[]) => (this.domainesSharedCollection = domaines));
  }
}
