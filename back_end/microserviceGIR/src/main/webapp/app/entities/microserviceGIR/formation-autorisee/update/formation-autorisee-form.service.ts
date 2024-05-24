import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFormationAutorisee, NewFormationAutorisee } from '../formation-autorisee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFormationAutorisee for edit and NewFormationAutoriseeFormGroupInput for create.
 */
type FormationAutoriseeFormGroupInput = IFormationAutorisee | PartialWithRequiredKeyOf<NewFormationAutorisee>;

type FormationAutoriseeFormDefaults = Pick<NewFormationAutorisee, 'id' | 'formations'>;

type FormationAutoriseeFormGroupContent = {
  id: FormControl<IFormationAutorisee['id'] | NewFormationAutorisee['id']>;
  dateDebut: FormControl<IFormationAutorisee['dateDebut']>;
  dateFin: FormControl<IFormationAutorisee['dateFin']>;
  enCoursYN: FormControl<IFormationAutorisee['enCoursYN']>;
  formations: FormControl<IFormationAutorisee['formations']>;
};

export type FormationAutoriseeFormGroup = FormGroup<FormationAutoriseeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FormationAutoriseeFormService {
  createFormationAutoriseeFormGroup(formationAutorisee: FormationAutoriseeFormGroupInput = { id: null }): FormationAutoriseeFormGroup {
    const formationAutoriseeRawValue = {
      ...this.getFormDefaults(),
      ...formationAutorisee,
    };
    return new FormGroup<FormationAutoriseeFormGroupContent>({
      id: new FormControl(
        { value: formationAutoriseeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebut: new FormControl(formationAutoriseeRawValue.dateDebut),
      dateFin: new FormControl(formationAutoriseeRawValue.dateFin),
      enCoursYN: new FormControl(formationAutoriseeRawValue.enCoursYN),
      formations: new FormControl(formationAutoriseeRawValue.formations ?? []),
    });
  }

  getFormationAutorisee(form: FormationAutoriseeFormGroup): IFormationAutorisee | NewFormationAutorisee {
    return form.getRawValue() as IFormationAutorisee | NewFormationAutorisee;
  }

  resetForm(form: FormationAutoriseeFormGroup, formationAutorisee: FormationAutoriseeFormGroupInput): void {
    const formationAutoriseeRawValue = { ...this.getFormDefaults(), ...formationAutorisee };
    form.reset(
      {
        ...formationAutoriseeRawValue,
        id: { value: formationAutoriseeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FormationAutoriseeFormDefaults {
    return {
      id: null,
      formations: [],
    };
  }
}
