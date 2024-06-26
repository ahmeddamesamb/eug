import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUfr, NewUfr } from '../ufr.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUfr for edit and NewUfrFormGroupInput for create.
 */
type UfrFormGroupInput = IUfr | PartialWithRequiredKeyOf<NewUfr>;

type UfrFormDefaults = Pick<NewUfr, 'id' | 'actifYN'>;

type UfrFormGroupContent = {
  id: FormControl<IUfr['id'] | NewUfr['id']>;
  libelleUfr: FormControl<IUfr['libelleUfr']>;
  sigleUfr: FormControl<IUfr['sigleUfr']>;
  prefixe: FormControl<IUfr['prefixe']>;
  actifYN: FormControl<IUfr['actifYN']>;
  universite: FormControl<IUfr['universite']>;
};

export type UfrFormGroup = FormGroup<UfrFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UfrFormService {
  createUfrFormGroup(ufr: UfrFormGroupInput = { id: null }): UfrFormGroup {
    const ufrRawValue = {
      ...this.getFormDefaults(),
      ...ufr,
    };
    return new FormGroup<UfrFormGroupContent>({
      id: new FormControl(
        { value: ufrRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleUfr: new FormControl(ufrRawValue.libelleUfr, {
        validators: [Validators.required],
      }),
      sigleUfr: new FormControl(ufrRawValue.sigleUfr, {
        validators: [Validators.required],
      }),
      prefixe: new FormControl(ufrRawValue.prefixe),
      actifYN: new FormControl(ufrRawValue.actifYN),
      universite: new FormControl(ufrRawValue.universite),
    });
  }

  getUfr(form: UfrFormGroup): IUfr | NewUfr {
    return form.getRawValue() as IUfr | NewUfr;
  }

  resetForm(form: UfrFormGroup, ufr: UfrFormGroupInput): void {
    const ufrRawValue = { ...this.getFormDefaults(), ...ufr };
    form.reset(
      {
        ...ufrRawValue,
        id: { value: ufrRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UfrFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
