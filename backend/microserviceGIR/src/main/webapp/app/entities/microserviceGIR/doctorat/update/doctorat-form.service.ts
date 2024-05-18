import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDoctorat, NewDoctorat } from '../doctorat.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDoctorat for edit and NewDoctoratFormGroupInput for create.
 */
type DoctoratFormGroupInput = IDoctorat | PartialWithRequiredKeyOf<NewDoctorat>;

type DoctoratFormDefaults = Pick<NewDoctorat, 'id'>;

type DoctoratFormGroupContent = {
  id: FormControl<IDoctorat['id'] | NewDoctorat['id']>;
  sujet: FormControl<IDoctorat['sujet']>;
  anneeInscriptionDoctorat: FormControl<IDoctorat['anneeInscriptionDoctorat']>;
  encadreurId: FormControl<IDoctorat['encadreurId']>;
  laboratoirId: FormControl<IDoctorat['laboratoirId']>;
};

export type DoctoratFormGroup = FormGroup<DoctoratFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DoctoratFormService {
  createDoctoratFormGroup(doctorat: DoctoratFormGroupInput = { id: null }): DoctoratFormGroup {
    const doctoratRawValue = {
      ...this.getFormDefaults(),
      ...doctorat,
    };
    return new FormGroup<DoctoratFormGroupContent>({
      id: new FormControl(
        { value: doctoratRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      sujet: new FormControl(doctoratRawValue.sujet),
      anneeInscriptionDoctorat: new FormControl(doctoratRawValue.anneeInscriptionDoctorat),
      encadreurId: new FormControl(doctoratRawValue.encadreurId),
      laboratoirId: new FormControl(doctoratRawValue.laboratoirId),
    });
  }

  getDoctorat(form: DoctoratFormGroup): IDoctorat | NewDoctorat {
    return form.getRawValue() as IDoctorat | NewDoctorat;
  }

  resetForm(form: DoctoratFormGroup, doctorat: DoctoratFormGroupInput): void {
    const doctoratRawValue = { ...this.getFormDefaults(), ...doctorat };
    form.reset(
      {
        ...doctoratRawValue,
        id: { value: doctoratRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DoctoratFormDefaults {
    return {
      id: null,
    };
  }
}
