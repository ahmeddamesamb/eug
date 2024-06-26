import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEnseignant, NewEnseignant } from '../enseignant.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEnseignant for edit and NewEnseignantFormGroupInput for create.
 */
type EnseignantFormGroupInput = IEnseignant | PartialWithRequiredKeyOf<NewEnseignant>;

type EnseignantFormDefaults = Pick<NewEnseignant, 'id'>;

type EnseignantFormGroupContent = {
  id: FormControl<IEnseignant['id'] | NewEnseignant['id']>;
  titreCoEncadreur: FormControl<IEnseignant['titreCoEncadreur']>;
  nom: FormControl<IEnseignant['nom']>;
  prenom: FormControl<IEnseignant['prenom']>;
  email: FormControl<IEnseignant['email']>;
  telephone: FormControl<IEnseignant['telephone']>;
  titresId: FormControl<IEnseignant['titresId']>;
  adresse: FormControl<IEnseignant['adresse']>;
  numeroPoste: FormControl<IEnseignant['numeroPoste']>;
  photo: FormControl<IEnseignant['photo']>;
};

export type EnseignantFormGroup = FormGroup<EnseignantFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EnseignantFormService {
  createEnseignantFormGroup(enseignant: EnseignantFormGroupInput = { id: null }): EnseignantFormGroup {
    const enseignantRawValue = {
      ...this.getFormDefaults(),
      ...enseignant,
    };
    return new FormGroup<EnseignantFormGroupContent>({
      id: new FormControl(
        { value: enseignantRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      titreCoEncadreur: new FormControl(enseignantRawValue.titreCoEncadreur),
      nom: new FormControl(enseignantRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(enseignantRawValue.prenom, {
        validators: [Validators.required],
      }),
      email: new FormControl(enseignantRawValue.email),
      telephone: new FormControl(enseignantRawValue.telephone),
      titresId: new FormControl(enseignantRawValue.titresId),
      adresse: new FormControl(enseignantRawValue.adresse),
      numeroPoste: new FormControl(enseignantRawValue.numeroPoste),
      photo: new FormControl(enseignantRawValue.photo),
    });
  }

  getEnseignant(form: EnseignantFormGroup): IEnseignant | NewEnseignant {
    return form.getRawValue() as IEnseignant | NewEnseignant;
  }

  resetForm(form: EnseignantFormGroup, enseignant: EnseignantFormGroupInput): void {
    const enseignantRawValue = { ...this.getFormDefaults(), ...enseignant };
    form.reset(
      {
        ...enseignantRawValue,
        id: { value: enseignantRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): EnseignantFormDefaults {
    return {
      id: null,
    };
  }
}
