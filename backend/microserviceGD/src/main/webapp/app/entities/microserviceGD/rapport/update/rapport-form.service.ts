import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRapport, NewRapport } from '../rapport.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRapport for edit and NewRapportFormGroupInput for create.
 */
type RapportFormGroupInput = IRapport | PartialWithRequiredKeyOf<NewRapport>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRapport | NewRapport> = Omit<T, 'dateRedaction'> & {
  dateRedaction?: string | null;
};

type RapportFormRawValue = FormValueOf<IRapport>;

type NewRapportFormRawValue = FormValueOf<NewRapport>;

type RapportFormDefaults = Pick<NewRapport, 'id' | 'dateRedaction'>;

type RapportFormGroupContent = {
  id: FormControl<RapportFormRawValue['id'] | NewRapport['id']>;
  libelleRapport: FormControl<RapportFormRawValue['libelleRapport']>;
  descriptionRapport: FormControl<RapportFormRawValue['descriptionRapport']>;
  contenuRapport: FormControl<RapportFormRawValue['contenuRapport']>;
  dateRedaction: FormControl<RapportFormRawValue['dateRedaction']>;
};

export type RapportFormGroup = FormGroup<RapportFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RapportFormService {
  createRapportFormGroup(rapport: RapportFormGroupInput = { id: null }): RapportFormGroup {
    const rapportRawValue = this.convertRapportToRapportRawValue({
      ...this.getFormDefaults(),
      ...rapport,
    });
    return new FormGroup<RapportFormGroupContent>({
      id: new FormControl(
        { value: rapportRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleRapport: new FormControl(rapportRawValue.libelleRapport),
      descriptionRapport: new FormControl(rapportRawValue.descriptionRapport),
      contenuRapport: new FormControl(rapportRawValue.contenuRapport),
      dateRedaction: new FormControl(rapportRawValue.dateRedaction),
    });
  }

  getRapport(form: RapportFormGroup): IRapport | NewRapport {
    return this.convertRapportRawValueToRapport(form.getRawValue() as RapportFormRawValue | NewRapportFormRawValue);
  }

  resetForm(form: RapportFormGroup, rapport: RapportFormGroupInput): void {
    const rapportRawValue = this.convertRapportToRapportRawValue({ ...this.getFormDefaults(), ...rapport });
    form.reset(
      {
        ...rapportRawValue,
        id: { value: rapportRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): RapportFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateRedaction: currentTime,
    };
  }

  private convertRapportRawValueToRapport(rawRapport: RapportFormRawValue | NewRapportFormRawValue): IRapport | NewRapport {
    return {
      ...rawRapport,
      dateRedaction: dayjs(rawRapport.dateRedaction, DATE_TIME_FORMAT),
    };
  }

  private convertRapportToRapportRawValue(
    rapport: IRapport | (Partial<NewRapport> & RapportFormDefaults),
  ): RapportFormRawValue | PartialWithRequiredKeyOf<NewRapportFormRawValue> {
    return {
      ...rapport,
      dateRedaction: rapport.dateRedaction ? rapport.dateRedaction.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
