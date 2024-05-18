import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ILycee, NewLycee } from '../lycee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ILycee for edit and NewLyceeFormGroupInput for create.
 */
type LyceeFormGroupInput = ILycee | PartialWithRequiredKeyOf<NewLycee>;

type LyceeFormDefaults = Pick<NewLycee, 'id'>;

type LyceeFormGroupContent = {
  id: FormControl<ILycee['id'] | NewLycee['id']>;
  nomLycee: FormControl<ILycee['nomLycee']>;
  codeLycee: FormControl<ILycee['codeLycee']>;
  villeLycee: FormControl<ILycee['villeLycee']>;
  academieLycee: FormControl<ILycee['academieLycee']>;
  centreExamen: FormControl<ILycee['centreExamen']>;
  region: FormControl<ILycee['region']>;
};

export type LyceeFormGroup = FormGroup<LyceeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class LyceeFormService {
  createLyceeFormGroup(lycee: LyceeFormGroupInput = { id: null }): LyceeFormGroup {
    const lyceeRawValue = {
      ...this.getFormDefaults(),
      ...lycee,
    };
    return new FormGroup<LyceeFormGroupContent>({
      id: new FormControl(
        { value: lyceeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomLycee: new FormControl(lyceeRawValue.nomLycee),
      codeLycee: new FormControl(lyceeRawValue.codeLycee),
      villeLycee: new FormControl(lyceeRawValue.villeLycee),
      academieLycee: new FormControl(lyceeRawValue.academieLycee),
      centreExamen: new FormControl(lyceeRawValue.centreExamen),
      region: new FormControl(lyceeRawValue.region),
    });
  }

  getLycee(form: LyceeFormGroup): ILycee | NewLycee {
    return form.getRawValue() as ILycee | NewLycee;
  }

  resetForm(form: LyceeFormGroup, lycee: LyceeFormGroupInput): void {
    const lyceeRawValue = { ...this.getFormDefaults(), ...lycee };
    form.reset(
      {
        ...lyceeRawValue,
        id: { value: lyceeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): LyceeFormDefaults {
    return {
      id: null,
    };
  }
}
