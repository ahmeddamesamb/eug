import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDisciplineSportiveEtudiant, NewDisciplineSportiveEtudiant } from '../discipline-sportive-etudiant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDisciplineSportiveEtudiant for edit and NewDisciplineSportiveEtudiantFormGroupInput for create.
 */
type DisciplineSportiveEtudiantFormGroupInput = IDisciplineSportiveEtudiant | PartialWithRequiredKeyOf<NewDisciplineSportiveEtudiant>;

type DisciplineSportiveEtudiantFormDefaults = Pick<NewDisciplineSportiveEtudiant, 'id' | 'licenceSportiveYN' | 'competitionUGBYN'>;

type DisciplineSportiveEtudiantFormGroupContent = {
  id: FormControl<IDisciplineSportiveEtudiant['id'] | NewDisciplineSportiveEtudiant['id']>;
  licenceSportiveYN: FormControl<IDisciplineSportiveEtudiant['licenceSportiveYN']>;
  competitionUGBYN: FormControl<IDisciplineSportiveEtudiant['competitionUGBYN']>;
  disciplineSportive: FormControl<IDisciplineSportiveEtudiant['disciplineSportive']>;
  etudiant: FormControl<IDisciplineSportiveEtudiant['etudiant']>;
};

export type DisciplineSportiveEtudiantFormGroup = FormGroup<DisciplineSportiveEtudiantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DisciplineSportiveEtudiantFormService {
  createDisciplineSportiveEtudiantFormGroup(
    disciplineSportiveEtudiant: DisciplineSportiveEtudiantFormGroupInput = { id: null },
  ): DisciplineSportiveEtudiantFormGroup {
    const disciplineSportiveEtudiantRawValue = {
      ...this.getFormDefaults(),
      ...disciplineSportiveEtudiant,
    };
    return new FormGroup<DisciplineSportiveEtudiantFormGroupContent>({
      id: new FormControl(
        { value: disciplineSportiveEtudiantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      licenceSportiveYN: new FormControl(disciplineSportiveEtudiantRawValue.licenceSportiveYN),
      competitionUGBYN: new FormControl(disciplineSportiveEtudiantRawValue.competitionUGBYN),
      disciplineSportive: new FormControl(disciplineSportiveEtudiantRawValue.disciplineSportive),
      etudiant: new FormControl(disciplineSportiveEtudiantRawValue.etudiant),
    });
  }

  getDisciplineSportiveEtudiant(form: DisciplineSportiveEtudiantFormGroup): IDisciplineSportiveEtudiant | NewDisciplineSportiveEtudiant {
    return form.getRawValue() as IDisciplineSportiveEtudiant | NewDisciplineSportiveEtudiant;
  }

  resetForm(form: DisciplineSportiveEtudiantFormGroup, disciplineSportiveEtudiant: DisciplineSportiveEtudiantFormGroupInput): void {
    const disciplineSportiveEtudiantRawValue = { ...this.getFormDefaults(), ...disciplineSportiveEtudiant };
    form.reset(
      {
        ...disciplineSportiveEtudiantRawValue,
        id: { value: disciplineSportiveEtudiantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DisciplineSportiveEtudiantFormDefaults {
    return {
      id: null,
      licenceSportiveYN: false,
      competitionUGBYN: false,
    };
  }
}
