import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInfoUserRessource, NewInfoUserRessource } from '../info-user-ressource.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInfoUserRessource for edit and NewInfoUserRessourceFormGroupInput for create.
 */
type InfoUserRessourceFormGroupInput = IInfoUserRessource | PartialWithRequiredKeyOf<NewInfoUserRessource>;

type InfoUserRessourceFormDefaults = Pick<NewInfoUserRessource, 'id' | 'enCoursYN'>;

type InfoUserRessourceFormGroupContent = {
  id: FormControl<IInfoUserRessource['id'] | NewInfoUserRessource['id']>;
  dateAjout: FormControl<IInfoUserRessource['dateAjout']>;
  enCoursYN: FormControl<IInfoUserRessource['enCoursYN']>;
  infosUser: FormControl<IInfoUserRessource['infosUser']>;
  ressource: FormControl<IInfoUserRessource['ressource']>;
};

export type InfoUserRessourceFormGroup = FormGroup<InfoUserRessourceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InfoUserRessourceFormService {
  createInfoUserRessourceFormGroup(infoUserRessource: InfoUserRessourceFormGroupInput = { id: null }): InfoUserRessourceFormGroup {
    const infoUserRessourceRawValue = {
      ...this.getFormDefaults(),
      ...infoUserRessource,
    };
    return new FormGroup<InfoUserRessourceFormGroupContent>({
      id: new FormControl(
        { value: infoUserRessourceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      dateAjout: new FormControl(infoUserRessourceRawValue.dateAjout, {
        validators: [Validators.required],
      }),
      enCoursYN: new FormControl(infoUserRessourceRawValue.enCoursYN, {
        validators: [Validators.required],
      }),
      infosUser: new FormControl(infoUserRessourceRawValue.infosUser),
      ressource: new FormControl(infoUserRessourceRawValue.ressource),
    });
  }

  getInfoUserRessource(form: InfoUserRessourceFormGroup): IInfoUserRessource | NewInfoUserRessource {
    return form.getRawValue() as IInfoUserRessource | NewInfoUserRessource;
  }

  resetForm(form: InfoUserRessourceFormGroup, infoUserRessource: InfoUserRessourceFormGroupInput): void {
    const infoUserRessourceRawValue = { ...this.getFormDefaults(), ...infoUserRessource };
    form.reset(
      {
        ...infoUserRessourceRawValue,
        id: { value: infoUserRessourceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InfoUserRessourceFormDefaults {
    return {
      id: null,
      enCoursYN: false,
    };
  }
}
