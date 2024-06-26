import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProfil, NewProfil } from '../profil.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProfil for edit and NewProfilFormGroupInput for create.
 */
type ProfilFormGroupInput = IProfil | PartialWithRequiredKeyOf<NewProfil>;

type ProfilFormDefaults = Pick<NewProfil, 'id' | 'actifYN'>;

type ProfilFormGroupContent = {
  id: FormControl<IProfil['id'] | NewProfil['id']>;
  libelle: FormControl<IProfil['libelle']>;
  dateAjout: FormControl<IProfil['dateAjout']>;
  actifYN: FormControl<IProfil['actifYN']>;
};

export type ProfilFormGroup = FormGroup<ProfilFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProfilFormService {
  createProfilFormGroup(profil: ProfilFormGroupInput = { id: null }): ProfilFormGroup {
    const profilRawValue = {
      ...this.getFormDefaults(),
      ...profil,
    };
    return new FormGroup<ProfilFormGroupContent>({
      id: new FormControl(
        { value: profilRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelle: new FormControl(profilRawValue.libelle, {
        validators: [Validators.required],
      }),
      dateAjout: new FormControl(profilRawValue.dateAjout, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(profilRawValue.actifYN, {
        validators: [Validators.required],
      }),
    });
  }

  getProfil(form: ProfilFormGroup): IProfil | NewProfil {
    return form.getRawValue() as IProfil | NewProfil;
  }

  resetForm(form: ProfilFormGroup, profil: ProfilFormGroupInput): void {
    const profilRawValue = { ...this.getFormDefaults(), ...profil };
    form.reset(
      {
        ...profilRawValue,
        id: { value: profilRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ProfilFormDefaults {
    return {
      id: null,
      actifYN: false,
    };
  }
}
