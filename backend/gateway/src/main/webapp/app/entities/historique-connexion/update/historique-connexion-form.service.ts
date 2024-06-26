import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHistoriqueConnexion, NewHistoriqueConnexion } from '../historique-connexion.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHistoriqueConnexion for edit and NewHistoriqueConnexionFormGroupInput for create.
 */
type HistoriqueConnexionFormGroupInput = IHistoriqueConnexion | PartialWithRequiredKeyOf<NewHistoriqueConnexion>;

type HistoriqueConnexionFormDefaults = Pick<NewHistoriqueConnexion, 'id' | 'actifYN'>;

type HistoriqueConnexionFormGroupContent = {
  id: FormControl<IHistoriqueConnexion['id'] | NewHistoriqueConnexion['id']>;
  dateDebutConnexion: FormControl<IHistoriqueConnexion['dateDebutConnexion']>;
  dateFinConnexion: FormControl<IHistoriqueConnexion['dateFinConnexion']>;
  adresseIp: FormControl<IHistoriqueConnexion['adresseIp']>;
  actifYN: FormControl<IHistoriqueConnexion['actifYN']>;
  infoUser: FormControl<IHistoriqueConnexion['infoUser']>;
};

export type HistoriqueConnexionFormGroup = FormGroup<HistoriqueConnexionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HistoriqueConnexionFormService {
  createHistoriqueConnexionFormGroup(historiqueConnexion: HistoriqueConnexionFormGroupInput = { id: null }): HistoriqueConnexionFormGroup {
    const historiqueConnexionRawValue = {
      ...this.getFormDefaults(),
      ...historiqueConnexion,
    };
    return new FormGroup<HistoriqueConnexionFormGroupContent>({
      id: new FormControl(
        { value: historiqueConnexionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateDebutConnexion: new FormControl(historiqueConnexionRawValue.dateDebutConnexion, {
        validators: [Validators.required],
      }),
      dateFinConnexion: new FormControl(historiqueConnexionRawValue.dateFinConnexion, {
        validators: [Validators.required],
      }),
      adresseIp: new FormControl(historiqueConnexionRawValue.adresseIp),
      actifYN: new FormControl(historiqueConnexionRawValue.actifYN, {
        validators: [Validators.required],
      }),
      infoUser: new FormControl(historiqueConnexionRawValue.infoUser),
    });
  }

  getHistoriqueConnexion(form: HistoriqueConnexionFormGroup): IHistoriqueConnexion | NewHistoriqueConnexion {
    return form.getRawValue() as IHistoriqueConnexion | NewHistoriqueConnexion;
  }

  resetForm(form: HistoriqueConnexionFormGroup, historiqueConnexion: HistoriqueConnexionFormGroupInput): void {
    const historiqueConnexionRawValue = { ...this.getFormDefaults(), ...historiqueConnexion };
    form.reset(
      {
        ...historiqueConnexionRawValue,
        id: { value: historiqueConnexionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): HistoriqueConnexionFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
