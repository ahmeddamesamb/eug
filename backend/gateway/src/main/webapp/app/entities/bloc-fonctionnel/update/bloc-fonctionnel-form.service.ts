import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBlocFonctionnel, NewBlocFonctionnel } from '../bloc-fonctionnel.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBlocFonctionnel for edit and NewBlocFonctionnelFormGroupInput for create.
 */
type BlocFonctionnelFormGroupInput = IBlocFonctionnel | PartialWithRequiredKeyOf<NewBlocFonctionnel>;

type BlocFonctionnelFormDefaults = Pick<NewBlocFonctionnel, 'id' | 'actifYN'>;

type BlocFonctionnelFormGroupContent = {
  id: FormControl<IBlocFonctionnel['id'] | NewBlocFonctionnel['id']>;
  libelleBloc: FormControl<IBlocFonctionnel['libelleBloc']>;
  dateAjoutBloc: FormControl<IBlocFonctionnel['dateAjoutBloc']>;
  actifYN: FormControl<IBlocFonctionnel['actifYN']>;
  service: FormControl<IBlocFonctionnel['service']>;
};

export type BlocFonctionnelFormGroup = FormGroup<BlocFonctionnelFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BlocFonctionnelFormService {
  createBlocFonctionnelFormGroup(blocFonctionnel: BlocFonctionnelFormGroupInput = { id: null }): BlocFonctionnelFormGroup {
    const blocFonctionnelRawValue = {
      ...this.getFormDefaults(),
      ...blocFonctionnel,
    };
    return new FormGroup<BlocFonctionnelFormGroupContent>({
      id: new FormControl(
        { value: blocFonctionnelRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleBloc: new FormControl(blocFonctionnelRawValue.libelleBloc, {
        validators: [Validators.required],
      }),
      dateAjoutBloc: new FormControl(blocFonctionnelRawValue.dateAjoutBloc, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(blocFonctionnelRawValue.actifYN, {
        validators: [Validators.required],
      }),
      service: new FormControl(blocFonctionnelRawValue.service),
    });
  }

  getBlocFonctionnel(form: BlocFonctionnelFormGroup): IBlocFonctionnel | NewBlocFonctionnel {
    return form.getRawValue() as IBlocFonctionnel | NewBlocFonctionnel;
  }

  resetForm(form: BlocFonctionnelFormGroup, blocFonctionnel: BlocFonctionnelFormGroupInput): void {
    const blocFonctionnelRawValue = { ...this.getFormDefaults(), ...blocFonctionnel };
    form.reset(
      {
        ...blocFonctionnelRawValue,
        id: { value: blocFonctionnelRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): BlocFonctionnelFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
