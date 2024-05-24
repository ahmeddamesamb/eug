import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILaboratoire, NewLaboratoire } from '../laboratoire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILaboratoire for edit and NewLaboratoireFormGroupInput for create.
 */
type LaboratoireFormGroupInput = ILaboratoire | PartialWithRequiredKeyOf<NewLaboratoire>;

type LaboratoireFormDefaults = Pick<NewLaboratoire, 'id'>;

type LaboratoireFormGroupContent = {
  id: FormControl<ILaboratoire['id'] | NewLaboratoire['id']>;
  nomLaboratoire: FormControl<ILaboratoire['nomLaboratoire']>;
  laboratoireCotutelleYN: FormControl<ILaboratoire['laboratoireCotutelleYN']>;
};

export type LaboratoireFormGroup = FormGroup<LaboratoireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LaboratoireFormService {
  createLaboratoireFormGroup(laboratoire: LaboratoireFormGroupInput = { id: null }): LaboratoireFormGroup {
    const laboratoireRawValue = {
      ...this.getFormDefaults(),
      ...laboratoire,
    };
    return new FormGroup<LaboratoireFormGroupContent>({
      id: new FormControl(
        { value: laboratoireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomLaboratoire: new FormControl(laboratoireRawValue.nomLaboratoire),
      laboratoireCotutelleYN: new FormControl(laboratoireRawValue.laboratoireCotutelleYN),
    });
  }

  getLaboratoire(form: LaboratoireFormGroup): ILaboratoire | NewLaboratoire {
    return form.getRawValue() as ILaboratoire | NewLaboratoire;
  }

  resetForm(form: LaboratoireFormGroup, laboratoire: LaboratoireFormGroupInput): void {
    const laboratoireRawValue = { ...this.getFormDefaults(), ...laboratoire };
    form.reset(
      {
        ...laboratoireRawValue,
        id: { value: laboratoireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LaboratoireFormDefaults {
    return {
      id: null,
    };
  }
}
