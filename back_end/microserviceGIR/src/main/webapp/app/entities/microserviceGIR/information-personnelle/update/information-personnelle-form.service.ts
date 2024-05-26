import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInformationPersonnelle, NewInformationPersonnelle } from '../information-personnelle.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInformationPersonnelle for edit and NewInformationPersonnelleFormGroupInput for create.
 */
type InformationPersonnelleFormGroupInput = IInformationPersonnelle | PartialWithRequiredKeyOf<NewInformationPersonnelle>;

type InformationPersonnelleFormDefaults = Pick<NewInformationPersonnelle, 'id'>;

type InformationPersonnelleFormGroupContent = {
  id: FormControl<IInformationPersonnelle['id'] | NewInformationPersonnelle['id']>;
  nomEtu: FormControl<IInformationPersonnelle['nomEtu']>;
  nomJeuneFilleEtu: FormControl<IInformationPersonnelle['nomJeuneFilleEtu']>;
  prenomEtu: FormControl<IInformationPersonnelle['prenomEtu']>;
  statutMarital: FormControl<IInformationPersonnelle['statutMarital']>;
  regime: FormControl<IInformationPersonnelle['regime']>;
  profession: FormControl<IInformationPersonnelle['profession']>;
  adresseEtu: FormControl<IInformationPersonnelle['adresseEtu']>;
  telEtu: FormControl<IInformationPersonnelle['telEtu']>;
  emailEtu: FormControl<IInformationPersonnelle['emailEtu']>;
  adresseParent: FormControl<IInformationPersonnelle['adresseParent']>;
  telParent: FormControl<IInformationPersonnelle['telParent']>;
  emailParent: FormControl<IInformationPersonnelle['emailParent']>;
  nomParent: FormControl<IInformationPersonnelle['nomParent']>;
  prenomParent: FormControl<IInformationPersonnelle['prenomParent']>;
  handicapYN: FormControl<IInformationPersonnelle['handicapYN']>;
  photo: FormControl<IInformationPersonnelle['photo']>;
  ordiPersoYN: FormControl<IInformationPersonnelle['ordiPersoYN']>;
  derniereModification: FormControl<IInformationPersonnelle['derniereModification']>;
  emailUser: FormControl<IInformationPersonnelle['emailUser']>;
  etudiant: FormControl<IInformationPersonnelle['etudiant']>;
  typeHandique: FormControl<IInformationPersonnelle['typeHandique']>;
  typeBourse: FormControl<IInformationPersonnelle['typeBourse']>;
};

export type InformationPersonnelleFormGroup = FormGroup<InformationPersonnelleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InformationPersonnelleFormService {
  createInformationPersonnelleFormGroup(
    informationPersonnelle: InformationPersonnelleFormGroupInput = { id: null },
  ): InformationPersonnelleFormGroup {
    const informationPersonnelleRawValue = {
      ...this.getFormDefaults(),
      ...informationPersonnelle,
    };
    return new FormGroup<InformationPersonnelleFormGroupContent>({
      id: new FormControl(
        { value: informationPersonnelleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomEtu: new FormControl(informationPersonnelleRawValue.nomEtu, {
        validators: [Validators.required],
      }),
      nomJeuneFilleEtu: new FormControl(informationPersonnelleRawValue.nomJeuneFilleEtu),
      prenomEtu: new FormControl(informationPersonnelleRawValue.prenomEtu, {
        validators: [Validators.required],
      }),
      statutMarital: new FormControl(informationPersonnelleRawValue.statutMarital, {
        validators: [Validators.required],
      }),
      regime: new FormControl(informationPersonnelleRawValue.regime),
      profession: new FormControl(informationPersonnelleRawValue.profession),
      adresseEtu: new FormControl(informationPersonnelleRawValue.adresseEtu, {
        validators: [Validators.required],
      }),
      telEtu: new FormControl(informationPersonnelleRawValue.telEtu),
      emailEtu: new FormControl(informationPersonnelleRawValue.emailEtu),
      adresseParent: new FormControl(informationPersonnelleRawValue.adresseParent),
      telParent: new FormControl(informationPersonnelleRawValue.telParent),
      emailParent: new FormControl(informationPersonnelleRawValue.emailParent),
      nomParent: new FormControl(informationPersonnelleRawValue.nomParent),
      prenomParent: new FormControl(informationPersonnelleRawValue.prenomParent),
      handicapYN: new FormControl(informationPersonnelleRawValue.handicapYN),
      photo: new FormControl(informationPersonnelleRawValue.photo),
      ordiPersoYN: new FormControl(informationPersonnelleRawValue.ordiPersoYN),
      derniereModification: new FormControl(informationPersonnelleRawValue.derniereModification),
      emailUser: new FormControl(informationPersonnelleRawValue.emailUser),
      etudiant: new FormControl(informationPersonnelleRawValue.etudiant),
      typeHandique: new FormControl(informationPersonnelleRawValue.typeHandique),
      typeBourse: new FormControl(informationPersonnelleRawValue.typeBourse),
    });
  }

  getInformationPersonnelle(form: InformationPersonnelleFormGroup): IInformationPersonnelle | NewInformationPersonnelle {
    return form.getRawValue() as IInformationPersonnelle | NewInformationPersonnelle;
  }

  resetForm(form: InformationPersonnelleFormGroup, informationPersonnelle: InformationPersonnelleFormGroupInput): void {
    const informationPersonnelleRawValue = { ...this.getFormDefaults(), ...informationPersonnelle };
    form.reset(
      {
        ...informationPersonnelleRawValue,
        id: { value: informationPersonnelleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InformationPersonnelleFormDefaults {
    return {
      id: null,
    };
  }
}
