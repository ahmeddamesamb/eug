import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFrais, NewFrais } from '../frais.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFrais for edit and NewFraisFormGroupInput for create.
 */
type FraisFormGroupInput = IFrais | PartialWithRequiredKeyOf<NewFrais>;

type FraisFormDefaults = Pick<
  NewFrais,
  'id' | 'fraisPourAssimileYN' | 'fraisPourExonererYN' | 'estEnApplicationYN' | 'actifYN' | 'universites'
>;

type FraisFormGroupContent = {
  id: FormControl<IFrais['id'] | NewFrais['id']>;
  valeurFrais: FormControl<IFrais['valeurFrais']>;
  descriptionFrais: FormControl<IFrais['descriptionFrais']>;
  fraisPourAssimileYN: FormControl<IFrais['fraisPourAssimileYN']>;
  fraisPourExonererYN: FormControl<IFrais['fraisPourExonererYN']>;
  dia: FormControl<IFrais['dia']>;
  dip: FormControl<IFrais['dip']>;
  fraisPrivee: FormControl<IFrais['fraisPrivee']>;
  dateApplication: FormControl<IFrais['dateApplication']>;
  dateFin: FormControl<IFrais['dateFin']>;
  estEnApplicationYN: FormControl<IFrais['estEnApplicationYN']>;
  actifYN: FormControl<IFrais['actifYN']>;
  typeFrais: FormControl<IFrais['typeFrais']>;
  typeCycle: FormControl<IFrais['typeCycle']>;
  universites: FormControl<IFrais['universites']>;
};

export type FraisFormGroup = FormGroup<FraisFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FraisFormService {
  createFraisFormGroup(frais: FraisFormGroupInput = { id: null }): FraisFormGroup {
    const fraisRawValue = {
      ...this.getFormDefaults(),
      ...frais,
    };
    return new FormGroup<FraisFormGroupContent>({
      id: new FormControl(
        { value: fraisRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      valeurFrais: new FormControl(fraisRawValue.valeurFrais, {
        validators: [Validators.required],
      }),
      descriptionFrais: new FormControl(fraisRawValue.descriptionFrais),
      fraisPourAssimileYN: new FormControl(fraisRawValue.fraisPourAssimileYN),
      fraisPourExonererYN: new FormControl(fraisRawValue.fraisPourExonererYN),
      dia: new FormControl(fraisRawValue.dia),
      dip: new FormControl(fraisRawValue.dip),
      fraisPrivee: new FormControl(fraisRawValue.fraisPrivee),
      dateApplication: new FormControl(fraisRawValue.dateApplication, {
        validators: [Validators.required],
      }),
      dateFin: new FormControl(fraisRawValue.dateFin),
      estEnApplicationYN: new FormControl(fraisRawValue.estEnApplicationYN, {
        validators: [Validators.required],
      }),
      actifYN: new FormControl(fraisRawValue.actifYN),
      typeFrais: new FormControl(fraisRawValue.typeFrais),
      typeCycle: new FormControl(fraisRawValue.typeCycle),
      universites: new FormControl(fraisRawValue.universites ?? []),
    });
  }

  getFrais(form: FraisFormGroup): IFrais | NewFrais {
    return form.getRawValue() as IFrais | NewFrais;
  }

  resetForm(form: FraisFormGroup, frais: FraisFormGroupInput): void {
    const fraisRawValue = { ...this.getFormDefaults(), ...frais };
    form.reset(
      {
        ...fraisRawValue,
        id: { value: fraisRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): FraisFormDefaults {
    return {
      id: null,
      fraisPourAssimileYN: false,
      fraisPourExonererYN: false,
      estEnApplicationYN: false,
      actifYN: false,
      universites: [],
    };
  }
}
