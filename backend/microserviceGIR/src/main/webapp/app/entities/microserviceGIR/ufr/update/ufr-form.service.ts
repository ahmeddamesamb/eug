import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUFR, NewUFR } from '../ufr.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUFR for edit and NewUFRFormGroupInput for create.
 */
type UFRFormGroupInput = IUFR | PartialWithRequiredKeyOf<NewUFR>;

type UFRFormDefaults = Pick<NewUFR, 'id'>;

type UFRFormGroupContent = {
  id: FormControl<IUFR['id'] | NewUFR['id']>;
  libeleUFR: FormControl<IUFR['libeleUFR']>;
  sigleUFR: FormControl<IUFR['sigleUFR']>;
  systemeLMDYN: FormControl<IUFR['systemeLMDYN']>;
  ordreStat: FormControl<IUFR['ordreStat']>;
  universite: FormControl<IUFR['universite']>;
};

export type UFRFormGroup = FormGroup<UFRFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UFRFormService {
  createUFRFormGroup(uFR: UFRFormGroupInput = { id: null }): UFRFormGroup {
    const uFRRawValue = {
      ...this.getFormDefaults(),
      ...uFR,
    };
    return new FormGroup<UFRFormGroupContent>({
      id: new FormControl(
        { value: uFRRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libeleUFR: new FormControl(uFRRawValue.libeleUFR),
      sigleUFR: new FormControl(uFRRawValue.sigleUFR),
      systemeLMDYN: new FormControl(uFRRawValue.systemeLMDYN),
      ordreStat: new FormControl(uFRRawValue.ordreStat),
      universite: new FormControl(uFRRawValue.universite),
    });
  }

  getUFR(form: UFRFormGroup): IUFR | NewUFR {
    return form.getRawValue() as IUFR | NewUFR;
  }

  resetForm(form: UFRFormGroup, uFR: UFRFormGroupInput): void {
    const uFRRawValue = { ...this.getFormDefaults(), ...uFR };
    form.reset(
      {
        ...uFRRawValue,
        id: { value: uFRRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UFRFormDefaults {
    return {
      id: null,
    };
  }
}
