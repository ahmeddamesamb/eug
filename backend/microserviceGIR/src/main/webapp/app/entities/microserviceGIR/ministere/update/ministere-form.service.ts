import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMinistere, NewMinistere } from '../ministere.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMinistere for edit and NewMinistereFormGroupInput for create.
 */
type MinistereFormGroupInput = IMinistere | PartialWithRequiredKeyOf<NewMinistere>;

type MinistereFormDefaults = Pick<NewMinistere, 'id'>;

type MinistereFormGroupContent = {
  id: FormControl<IMinistere['id'] | NewMinistere['id']>;
  nomMinistere: FormControl<IMinistere['nomMinistere']>;
  sigleMinistere: FormControl<IMinistere['sigleMinistere']>;
  dateDebut: FormControl<IMinistere['dateDebut']>;
  dateFin: FormControl<IMinistere['dateFin']>;
  enCours: FormControl<IMinistere['enCours']>;
};

export type MinistereFormGroup = FormGroup<MinistereFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MinistereFormService {
  createMinistereFormGroup(ministere: MinistereFormGroupInput = { id: null }): MinistereFormGroup {
    const ministereRawValue = {
      ...this.getFormDefaults(),
      ...ministere,
    };
    return new FormGroup<MinistereFormGroupContent>({
      id: new FormControl(
        { value: ministereRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomMinistere: new FormControl(ministereRawValue.nomMinistere),
      sigleMinistere: new FormControl(ministereRawValue.sigleMinistere),
      dateDebut: new FormControl(ministereRawValue.dateDebut),
      dateFin: new FormControl(ministereRawValue.dateFin),
      enCours: new FormControl(ministereRawValue.enCours),
    });
  }

  getMinistere(form: MinistereFormGroup): IMinistere | NewMinistere {
    return form.getRawValue() as IMinistere | NewMinistere;
  }

  resetForm(form: MinistereFormGroup, ministere: MinistereFormGroupInput): void {
    const ministereRawValue = { ...this.getFormDefaults(), ...ministere };
    form.reset(
      {
        ...ministereRawValue,
        id: { value: ministereRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MinistereFormDefaults {
    return {
      id: null,
    };
  }
}
