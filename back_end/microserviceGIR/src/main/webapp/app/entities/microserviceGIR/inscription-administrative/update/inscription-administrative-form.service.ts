import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInscriptionAdministrative, NewInscriptionAdministrative } from '../inscription-administrative.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscriptionAdministrative for edit and NewInscriptionAdministrativeFormGroupInput for create.
 */
type InscriptionAdministrativeFormGroupInput = IInscriptionAdministrative | PartialWithRequiredKeyOf<NewInscriptionAdministrative>;

type InscriptionAdministrativeFormDefaults = Pick<NewInscriptionAdministrative, 'id'>;

type InscriptionAdministrativeFormGroupContent = {
  id: FormControl<IInscriptionAdministrative['id'] | NewInscriptionAdministrative['id']>;
  nouveauInscritYN: FormControl<IInscriptionAdministrative['nouveauInscritYN']>;
  repriseYN: FormControl<IInscriptionAdministrative['repriseYN']>;
  autoriseYN: FormControl<IInscriptionAdministrative['autoriseYN']>;
  ordreInscription: FormControl<IInscriptionAdministrative['ordreInscription']>;
  typeAdmission: FormControl<IInscriptionAdministrative['typeAdmission']>;
  anneeAcademique: FormControl<IInscriptionAdministrative['anneeAcademique']>;
  etudiant: FormControl<IInscriptionAdministrative['etudiant']>;
};

export type InscriptionAdministrativeFormGroup = FormGroup<InscriptionAdministrativeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionAdministrativeFormService {
  createInscriptionAdministrativeFormGroup(
    inscriptionAdministrative: InscriptionAdministrativeFormGroupInput = { id: null },
  ): InscriptionAdministrativeFormGroup {
    const inscriptionAdministrativeRawValue = {
      ...this.getFormDefaults(),
      ...inscriptionAdministrative,
    };
    return new FormGroup<InscriptionAdministrativeFormGroupContent>({
      id: new FormControl(
        { value: inscriptionAdministrativeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nouveauInscritYN: new FormControl(inscriptionAdministrativeRawValue.nouveauInscritYN),
      repriseYN: new FormControl(inscriptionAdministrativeRawValue.repriseYN),
      autoriseYN: new FormControl(inscriptionAdministrativeRawValue.autoriseYN),
      ordreInscription: new FormControl(inscriptionAdministrativeRawValue.ordreInscription),
      typeAdmission: new FormControl(inscriptionAdministrativeRawValue.typeAdmission),
      anneeAcademique: new FormControl(inscriptionAdministrativeRawValue.anneeAcademique),
      etudiant: new FormControl(inscriptionAdministrativeRawValue.etudiant),
    });
  }

  getInscriptionAdministrative(form: InscriptionAdministrativeFormGroup): IInscriptionAdministrative | NewInscriptionAdministrative {
    return form.getRawValue() as IInscriptionAdministrative | NewInscriptionAdministrative;
  }

  resetForm(form: InscriptionAdministrativeFormGroup, inscriptionAdministrative: InscriptionAdministrativeFormGroupInput): void {
    const inscriptionAdministrativeRawValue = { ...this.getFormDefaults(), ...inscriptionAdministrative };
    form.reset(
      {
        ...inscriptionAdministrativeRawValue,
        id: { value: inscriptionAdministrativeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InscriptionAdministrativeFormDefaults {
    return {
      id: null,
    };
  }
}
