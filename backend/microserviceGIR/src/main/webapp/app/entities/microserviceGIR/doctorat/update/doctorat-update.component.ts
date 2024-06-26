import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDoctorat } from '../doctorat.model';
import { DoctoratService } from '../service/doctorat.service';
import { DoctoratFormService, DoctoratFormGroup } from './doctorat-form.service';

@Component({
  standalone: true,
  selector: 'ugb-doctorat-update',
  templateUrl: './doctorat-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DoctoratUpdateComponent implements OnInit {
  isSaving = false;
  doctorat: IDoctorat | null = null;

  editForm: DoctoratFormGroup = this.doctoratFormService.createDoctoratFormGroup();

  constructor(
    protected doctoratService: DoctoratService,
    protected doctoratFormService: DoctoratFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ doctorat }) => {
      this.doctorat = doctorat;
      if (doctorat) {
        this.updateForm(doctorat);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const doctorat = this.doctoratFormService.getDoctorat(this.editForm);
    if (doctorat.id !== null) {
      this.subscribeToSaveResponse(this.doctoratService.update(doctorat));
    } else {
      this.subscribeToSaveResponse(this.doctoratService.create(doctorat));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDoctorat>>): void {
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

  protected updateForm(doctorat: IDoctorat): void {
    this.doctorat = doctorat;
    this.doctoratFormService.resetForm(this.editForm, doctorat);
  }
}
