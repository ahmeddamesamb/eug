import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRessourceAide, NewRessourceAide } from '../ressource-aide.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRessourceAide for edit and NewRessourceAideFormGroupInput for create.
 */
type RessourceAideFormGroupInput = IRessourceAide | PartialWithRequiredKeyOf<NewRessourceAide>;

type RessourceAideFormDefaults = Pick<NewRessourceAide, 'id'>;

type RessourceAideFormGroupContent = {
  id: FormControl<IRessourceAide['id'] | NewRessourceAide['id']>;
  nom: FormControl<IRessourceAide['nom']>;
  libelle: FormControl<IRessourceAide['libelle']>;
};

export type RessourceAideFormGroup = FormGroup<RessourceAideFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RessourceAideFormService {
  createRessourceAideFormGroup(ressourceAide: RessourceAideFormGroupInput = { id: null }): RessourceAideFormGroup {
    const ressourceAideRawValue = {
      ...this.getFormDefaults(),
      ...ressourceAide,
    };
    return new FormGroup<RessourceAideFormGroupContent>({
      id: new FormControl(
        { value: ressourceAideRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nom: new FormControl(ressourceAideRawValue.nom, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(ressourceAideRawValue.libelle),
    });
  }

  getRessourceAide(form: RessourceAideFormGroup): IRessourceAide | NewRessourceAide {
    return form.getRawValue() as IRessourceAide | NewRessourceAide;
  }

  resetForm(form: RessourceAideFormGroup, ressourceAide: RessourceAideFormGroupInput): void {
    const ressourceAideRawValue = { ...this.getFormDefaults(), ...ressourceAide };
    form.reset(
      {
        ...ressourceAideRawValue,
        id: { value: ressourceAideRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RessourceAideFormDefaults {
    return {
      id: null,
    };
  }
}
