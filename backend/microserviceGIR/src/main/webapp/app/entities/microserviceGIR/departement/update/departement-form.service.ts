import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDepartement, NewDepartement } from '../departement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDepartement for edit and NewDepartementFormGroupInput for create.
 */
type DepartementFormGroupInput = IDepartement | PartialWithRequiredKeyOf<NewDepartement>;

type DepartementFormDefaults = Pick<NewDepartement, 'id' | 'actifYN'>;

type DepartementFormGroupContent = {
  id: FormControl<IDepartement['id'] | NewDepartement['id']>;
  nomDepatement: FormControl<IDepartement['nomDepatement']>;
  actifYN: FormControl<IDepartement['actifYN']>;
  ufr: FormControl<IDepartement['ufr']>;
};

export type DepartementFormGroup = FormGroup<DepartementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DepartementFormService {
  createDepartementFormGroup(departement: DepartementFormGroupInput = { id: null }): DepartementFormGroup {
    const departementRawValue = {
      ...this.getFormDefaults(),
      ...departement,
    };
    return new FormGroup<DepartementFormGroupContent>({
      id: new FormControl(
        { value: departementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomDepatement: new FormControl(departementRawValue.nomDepatement, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(departementRawValue.actifYN),
      ufr: new FormControl(departementRawValue.ufr),
    });
  }

  getDepartement(form: DepartementFormGroup): IDepartement | NewDepartement {
    return form.getRawValue() as IDepartement | NewDepartement;
  }

  resetForm(form: DepartementFormGroup, departement: DepartementFormGroupInput): void {
    const departementRawValue = { ...this.getFormDefaults(), ...departement };
    form.reset(
      {
        ...departementRawValue,
        id: { value: departementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DepartementFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
