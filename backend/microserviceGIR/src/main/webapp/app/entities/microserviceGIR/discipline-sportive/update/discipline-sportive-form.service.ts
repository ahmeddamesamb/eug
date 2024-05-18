import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDisciplineSportive, NewDisciplineSportive } from '../discipline-sportive.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDisciplineSportive for edit and NewDisciplineSportiveFormGroupInput for create.
 */
type DisciplineSportiveFormGroupInput = IDisciplineSportive | PartialWithRequiredKeyOf<NewDisciplineSportive>;

type DisciplineSportiveFormDefaults = Pick<NewDisciplineSportive, 'id'>;

type DisciplineSportiveFormGroupContent = {
  id: FormControl<IDisciplineSportive['id'] | NewDisciplineSportive['id']>;
  libelleDisciplineSportive: FormControl<IDisciplineSportive['libelleDisciplineSportive']>;
};

export type DisciplineSportiveFormGroup = FormGroup<DisciplineSportiveFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DisciplineSportiveFormService {
  createDisciplineSportiveFormGroup(disciplineSportive: DisciplineSportiveFormGroupInput = { id: null }): DisciplineSportiveFormGroup {
    const disciplineSportiveRawValue = {
      ...this.getFormDefaults(),
      ...disciplineSportive,
    };
    return new FormGroup<DisciplineSportiveFormGroupContent>({
      id: new FormControl(
        { value: disciplineSportiveRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleDisciplineSportive: new FormControl(disciplineSportiveRawValue.libelleDisciplineSportive),
    });
  }

  getDisciplineSportive(form: DisciplineSportiveFormGroup): IDisciplineSportive | NewDisciplineSportive {
    return form.getRawValue() as IDisciplineSportive | NewDisciplineSportive;
  }

  resetForm(form: DisciplineSportiveFormGroup, disciplineSportive: DisciplineSportiveFormGroupInput): void {
    const disciplineSportiveRawValue = { ...this.getFormDefaults(), ...disciplineSportive };
    form.reset(
      {
        ...disciplineSportiveRawValue,
        id: { value: disciplineSportiveRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DisciplineSportiveFormDefaults {
    return {
      id: null,
    };
  }
}
