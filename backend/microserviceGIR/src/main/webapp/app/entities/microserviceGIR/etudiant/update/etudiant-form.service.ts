import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEtudiant, NewEtudiant } from '../etudiant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEtudiant for edit and NewEtudiantFormGroupInput for create.
 */
type EtudiantFormGroupInput = IEtudiant | PartialWithRequiredKeyOf<NewEtudiant>;

type EtudiantFormDefaults = Pick<NewEtudiant, 'id'>;

type EtudiantFormGroupContent = {
  id: FormControl<IEtudiant['id'] | NewEtudiant['id']>;
  codeEtu: FormControl<IEtudiant['codeEtu']>;
  ine: FormControl<IEtudiant['ine']>;
  codeBU: FormControl<IEtudiant['codeBU']>;
  emailUGB: FormControl<IEtudiant['emailUGB']>;
  dateNaissEtu: FormControl<IEtudiant['dateNaissEtu']>;
  lieuNaissEtu: FormControl<IEtudiant['lieuNaissEtu']>;
  sexe: FormControl<IEtudiant['sexe']>;
  numDocIdentite: FormControl<IEtudiant['numDocIdentite']>;
  assimileYN: FormControl<IEtudiant['assimileYN']>;
  exonereYN: FormControl<IEtudiant['exonereYN']>;
  region: FormControl<IEtudiant['region']>;
  typeSelection: FormControl<IEtudiant['typeSelection']>;
  lycee: FormControl<IEtudiant['lycee']>;
};

export type EtudiantFormGroup = FormGroup<EtudiantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EtudiantFormService {
  createEtudiantFormGroup(etudiant: EtudiantFormGroupInput = { id: null }): EtudiantFormGroup {
    const etudiantRawValue = {
      ...this.getFormDefaults(),
      ...etudiant,
    };
    return new FormGroup<EtudiantFormGroupContent>({
      id: new FormControl(
        { value: etudiantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      codeEtu: new FormControl(etudiantRawValue.codeEtu, {
        validators: [Validators.required],
      }),
      ine: new FormControl(etudiantRawValue.ine, {
        validators: [Validators.required],
      }),
      codeBU: new FormControl(etudiantRawValue.codeBU, {
        validators: [Validators.required],
      }),
      emailUGB: new FormControl(etudiantRawValue.emailUGB),
      dateNaissEtu: new FormControl(etudiantRawValue.dateNaissEtu),
      lieuNaissEtu: new FormControl(etudiantRawValue.lieuNaissEtu, {
        validators: [Validators.required],
      }),
      sexe: new FormControl(etudiantRawValue.sexe, {
        validators: [Validators.required],
      }),
      numDocIdentite: new FormControl(etudiantRawValue.numDocIdentite),
      assimileYN: new FormControl(etudiantRawValue.assimileYN, {
        validators: [Validators.required],
      }),
      exonereYN: new FormControl(etudiantRawValue.exonereYN, {
        validators: [Validators.required],
      }),
      region: new FormControl(etudiantRawValue.region),
      typeSelection: new FormControl(etudiantRawValue.typeSelection),
      lycee: new FormControl(etudiantRawValue.lycee),
    });
  }

  getEtudiant(form: EtudiantFormGroup): IEtudiant | NewEtudiant {
    return form.getRawValue() as IEtudiant | NewEtudiant;
  }

  resetForm(form: EtudiantFormGroup, etudiant: EtudiantFormGroupInput): void {
    const etudiantRawValue = { ...this.getFormDefaults(), ...etudiant };
    form.reset(
      {
        ...etudiantRawValue,
        id: { value: etudiantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EtudiantFormDefaults {
    return {
      id: null,
    };
  }
}
