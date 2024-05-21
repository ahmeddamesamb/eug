import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInscriptionDoctorat, NewInscriptionDoctorat } from '../inscription-doctorat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInscriptionDoctorat for edit and NewInscriptionDoctoratFormGroupInput for create.
 */
type InscriptionDoctoratFormGroupInput = IInscriptionDoctorat | PartialWithRequiredKeyOf<NewInscriptionDoctorat>;

type InscriptionDoctoratFormDefaults = Pick<NewInscriptionDoctorat, 'id'>;

type InscriptionDoctoratFormGroupContent = {
  id: FormControl<IInscriptionDoctorat['id'] | NewInscriptionDoctorat['id']>;
  sourceFinancement: FormControl<IInscriptionDoctorat['sourceFinancement']>;
  coEncadreurId: FormControl<IInscriptionDoctorat['coEncadreurId']>;
  nombreInscription: FormControl<IInscriptionDoctorat['nombreInscription']>;
  doctorat: FormControl<IInscriptionDoctorat['doctorat']>;
  inscriptionAdministrativeFormation: FormControl<IInscriptionDoctorat['inscriptionAdministrativeFormation']>;
};

export type InscriptionDoctoratFormGroup = FormGroup<InscriptionDoctoratFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InscriptionDoctoratFormService {
  createInscriptionDoctoratFormGroup(inscriptionDoctorat: InscriptionDoctoratFormGroupInput = { id: null }): InscriptionDoctoratFormGroup {
    const inscriptionDoctoratRawValue = {
      ...this.getFormDefaults(),
      ...inscriptionDoctorat,
    };
    return new FormGroup<InscriptionDoctoratFormGroupContent>({
      id: new FormControl(
        { value: inscriptionDoctoratRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sourceFinancement: new FormControl(inscriptionDoctoratRawValue.sourceFinancement),
      coEncadreurId: new FormControl(inscriptionDoctoratRawValue.coEncadreurId),
      nombreInscription: new FormControl(inscriptionDoctoratRawValue.nombreInscription),
      doctorat: new FormControl(inscriptionDoctoratRawValue.doctorat),
      inscriptionAdministrativeFormation: new FormControl(inscriptionDoctoratRawValue.inscriptionAdministrativeFormation),
    });
  }

  getInscriptionDoctorat(form: InscriptionDoctoratFormGroup): IInscriptionDoctorat | NewInscriptionDoctorat {
    return form.getRawValue() as IInscriptionDoctorat | NewInscriptionDoctorat;
  }

  resetForm(form: InscriptionDoctoratFormGroup, inscriptionDoctorat: InscriptionDoctoratFormGroupInput): void {
    const inscriptionDoctoratRawValue = { ...this.getFormDefaults(), ...inscriptionDoctorat };
    form.reset(
      {
        ...inscriptionDoctoratRawValue,
        id: { value: inscriptionDoctoratRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InscriptionDoctoratFormDefaults {
    return {
      id: null,
    };
  }
}
