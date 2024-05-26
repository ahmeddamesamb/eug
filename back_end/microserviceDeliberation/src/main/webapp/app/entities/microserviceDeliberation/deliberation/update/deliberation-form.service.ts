import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDeliberation, NewDeliberation } from '../deliberation.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDeliberation for edit and NewDeliberationFormGroupInput for create.
 */
type DeliberationFormGroupInput = IDeliberation | PartialWithRequiredKeyOf<NewDeliberation>;

type DeliberationFormDefaults = Pick<NewDeliberation, 'id'>;

type DeliberationFormGroupContent = {
  id: FormControl<IDeliberation['id'] | NewDeliberation['id']>;
  estValideeYN: FormControl<IDeliberation['estValideeYN']>;
  pvDeliberation: FormControl<IDeliberation['pvDeliberation']>;
  pvDeliberationContentType: FormControl<IDeliberation['pvDeliberationContentType']>;
};

export type DeliberationFormGroup = FormGroup<DeliberationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DeliberationFormService {
  createDeliberationFormGroup(deliberation: DeliberationFormGroupInput = { id: null }): DeliberationFormGroup {
    const deliberationRawValue = {
      ...this.getFormDefaults(),
      ...deliberation,
    };
    return new FormGroup<DeliberationFormGroupContent>({
      id: new FormControl(
        { value: deliberationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      estValideeYN: new FormControl(deliberationRawValue.estValideeYN),
      pvDeliberation: new FormControl(deliberationRawValue.pvDeliberation),
      pvDeliberationContentType: new FormControl(deliberationRawValue.pvDeliberationContentType),
    });
  }

  getDeliberation(form: DeliberationFormGroup): IDeliberation | NewDeliberation {
    return form.getRawValue() as IDeliberation | NewDeliberation;
  }

  resetForm(form: DeliberationFormGroup, deliberation: DeliberationFormGroupInput): void {
    const deliberationRawValue = { ...this.getFormDefaults(), ...deliberation };
    form.reset(
      {
        ...deliberationRawValue,
        id: { value: deliberationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DeliberationFormDefaults {
    return {
      id: null,
    };
  }
}
