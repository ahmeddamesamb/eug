import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProgrammationInscription, NewProgrammationInscription } from '../programmation-inscription.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProgrammationInscription for edit and NewProgrammationInscriptionFormGroupInput for create.
 */
type ProgrammationInscriptionFormGroupInput = IProgrammationInscription | PartialWithRequiredKeyOf<NewProgrammationInscription>;

type ProgrammationInscriptionFormDefaults = Pick<NewProgrammationInscription, 'id' | 'ouvertYN' | 'forclosClasseYN' | 'actifYN'>;

type ProgrammationInscriptionFormGroupContent = {
  id: FormControl<IProgrammationInscription['id'] | NewProgrammationInscription['id']>;
  libelleProgrammation: FormControl<IProgrammationInscription['libelleProgrammation']>;
  dateDebutProgrammation: FormControl<IProgrammationInscription['dateDebutProgrammation']>;
  dateFinProgrammation: FormControl<IProgrammationInscription['dateFinProgrammation']>;
  ouvertYN: FormControl<IProgrammationInscription['ouvertYN']>;
  emailUser: FormControl<IProgrammationInscription['emailUser']>;
  dateForclosClasse: FormControl<IProgrammationInscription['dateForclosClasse']>;
  forclosClasseYN: FormControl<IProgrammationInscription['forclosClasseYN']>;
  actifYN: FormControl<IProgrammationInscription['actifYN']>;
  anneeAcademique: FormControl<IProgrammationInscription['anneeAcademique']>;
  formation: FormControl<IProgrammationInscription['formation']>;
  campagne: FormControl<IProgrammationInscription['campagne']>;
};

export type ProgrammationInscriptionFormGroup = FormGroup<ProgrammationInscriptionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProgrammationInscriptionFormService {
  createProgrammationInscriptionFormGroup(
    programmationInscription: ProgrammationInscriptionFormGroupInput = { id: null },
  ): ProgrammationInscriptionFormGroup {
    const programmationInscriptionRawValue = {
      ...this.getFormDefaults(),
      ...programmationInscription,
    };
    return new FormGroup<ProgrammationInscriptionFormGroupContent>({
      id: new FormControl(
        { value: programmationInscriptionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleProgrammation: new FormControl(programmationInscriptionRawValue.libelleProgrammation),
      dateDebutProgrammation: new FormControl(programmationInscriptionRawValue.dateDebutProgrammation),
      dateFinProgrammation: new FormControl(programmationInscriptionRawValue.dateFinProgrammation),
      ouvertYN: new FormControl(programmationInscriptionRawValue.ouvertYN),
      emailUser: new FormControl(programmationInscriptionRawValue.emailUser),
      dateForclosClasse: new FormControl(programmationInscriptionRawValue.dateForclosClasse),
      forclosClasseYN: new FormControl(programmationInscriptionRawValue.forclosClasseYN),
      actifYN: new FormControl(programmationInscriptionRawValue.actifYN),
      anneeAcademique: new FormControl(programmationInscriptionRawValue.anneeAcademique),
      formation: new FormControl(programmationInscriptionRawValue.formation),
      campagne: new FormControl(programmationInscriptionRawValue.campagne),
    });
  }

  getProgrammationInscription(form: ProgrammationInscriptionFormGroup): IProgrammationInscription | NewProgrammationInscription {
    return form.getRawValue() as IProgrammationInscription | NewProgrammationInscription;
  }

  resetForm(form: ProgrammationInscriptionFormGroup, programmationInscription: ProgrammationInscriptionFormGroupInput): void {
    const programmationInscriptionRawValue = { ...this.getFormDefaults(), ...programmationInscription };
    form.reset(
      {
        ...programmationInscriptionRawValue,
        id: { value: programmationInscriptionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProgrammationInscriptionFormDefaults {
    return {
      id: null,
      ouvertYN: false,
      forclosClasseYN: false,
      actifYN: false,
    };
  }
}
