import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInformationImage, NewInformationImage } from '../information-image.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInformationImage for edit and NewInformationImageFormGroupInput for create.
 */
type InformationImageFormGroupInput = IInformationImage | PartialWithRequiredKeyOf<NewInformationImage>;

type InformationImageFormDefaults = Pick<NewInformationImage, 'id'>;

type InformationImageFormGroupContent = {
  id: FormControl<IInformationImage['id'] | NewInformationImage['id']>;
  description: FormControl<IInformationImage['description']>;
  cheminPath: FormControl<IInformationImage['cheminPath']>;
  cheminFile: FormControl<IInformationImage['cheminFile']>;
  enCoursYN: FormControl<IInformationImage['enCoursYN']>;
};

export type InformationImageFormGroup = FormGroup<InformationImageFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InformationImageFormService {
  createInformationImageFormGroup(informationImage: InformationImageFormGroupInput = { id: null }): InformationImageFormGroup {
    const informationImageRawValue = {
      ...this.getFormDefaults(),
      ...informationImage,
    };
    return new FormGroup<InformationImageFormGroupContent>({
      id: new FormControl(
        { value: informationImageRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      description: new FormControl(informationImageRawValue.description),
      cheminPath: new FormControl(informationImageRawValue.cheminPath, {
        validators: [Validators.required],
      }),
      cheminFile: new FormControl(informationImageRawValue.cheminFile, {
        validators: [Validators.required],
      }),
      enCoursYN: new FormControl(informationImageRawValue.enCoursYN),
    });
  }

  getInformationImage(form: InformationImageFormGroup): IInformationImage | NewInformationImage {
    return form.getRawValue() as IInformationImage | NewInformationImage;
  }

  resetForm(form: InformationImageFormGroup, informationImage: InformationImageFormGroupInput): void {
    const informationImageRawValue = { ...this.getFormDefaults(), ...informationImage };
    form.reset(
      {
        ...informationImageRawValue,
        id: { value: informationImageRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): InformationImageFormDefaults {
    return {
      id: null,
    };
  }
}
