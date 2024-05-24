import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDocumentDelivre, NewDocumentDelivre } from '../document-delivre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDocumentDelivre for edit and NewDocumentDelivreFormGroupInput for create.
 */
type DocumentDelivreFormGroupInput = IDocumentDelivre | PartialWithRequiredKeyOf<NewDocumentDelivre>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDocumentDelivre | NewDocumentDelivre> = Omit<T, 'anneeDoc' | 'dateEnregistrement'> & {
  anneeDoc?: string | null;
  dateEnregistrement?: string | null;
};

type DocumentDelivreFormRawValue = FormValueOf<IDocumentDelivre>;

type NewDocumentDelivreFormRawValue = FormValueOf<NewDocumentDelivre>;

type DocumentDelivreFormDefaults = Pick<NewDocumentDelivre, 'id' | 'anneeDoc' | 'dateEnregistrement'>;

type DocumentDelivreFormGroupContent = {
  id: FormControl<DocumentDelivreFormRawValue['id'] | NewDocumentDelivre['id']>;
  libelleDoc: FormControl<DocumentDelivreFormRawValue['libelleDoc']>;
  anneeDoc: FormControl<DocumentDelivreFormRawValue['anneeDoc']>;
  dateEnregistrement: FormControl<DocumentDelivreFormRawValue['dateEnregistrement']>;
  typeDocument: FormControl<DocumentDelivreFormRawValue['typeDocument']>;
};

export type DocumentDelivreFormGroup = FormGroup<DocumentDelivreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DocumentDelivreFormService {
  createDocumentDelivreFormGroup(documentDelivre: DocumentDelivreFormGroupInput = { id: null }): DocumentDelivreFormGroup {
    const documentDelivreRawValue = this.convertDocumentDelivreToDocumentDelivreRawValue({
      ...this.getFormDefaults(),
      ...documentDelivre,
    });
    return new FormGroup<DocumentDelivreFormGroupContent>({
      id: new FormControl(
        { value: documentDelivreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleDoc: new FormControl(documentDelivreRawValue.libelleDoc),
      anneeDoc: new FormControl(documentDelivreRawValue.anneeDoc),
      dateEnregistrement: new FormControl(documentDelivreRawValue.dateEnregistrement),
      typeDocument: new FormControl(documentDelivreRawValue.typeDocument),
    });
  }

  getDocumentDelivre(form: DocumentDelivreFormGroup): IDocumentDelivre | NewDocumentDelivre {
    return this.convertDocumentDelivreRawValueToDocumentDelivre(
      form.getRawValue() as DocumentDelivreFormRawValue | NewDocumentDelivreFormRawValue,
    );
  }

  resetForm(form: DocumentDelivreFormGroup, documentDelivre: DocumentDelivreFormGroupInput): void {
    const documentDelivreRawValue = this.convertDocumentDelivreToDocumentDelivreRawValue({ ...this.getFormDefaults(), ...documentDelivre });
    form.reset(
      {
        ...documentDelivreRawValue,
        id: { value: documentDelivreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DocumentDelivreFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      anneeDoc: currentTime,
      dateEnregistrement: currentTime,
    };
  }

  private convertDocumentDelivreRawValueToDocumentDelivre(
    rawDocumentDelivre: DocumentDelivreFormRawValue | NewDocumentDelivreFormRawValue,
  ): IDocumentDelivre | NewDocumentDelivre {
    return {
      ...rawDocumentDelivre,
      anneeDoc: dayjs(rawDocumentDelivre.anneeDoc, DATE_TIME_FORMAT),
      dateEnregistrement: dayjs(rawDocumentDelivre.dateEnregistrement, DATE_TIME_FORMAT),
    };
  }

  private convertDocumentDelivreToDocumentDelivreRawValue(
    documentDelivre: IDocumentDelivre | (Partial<NewDocumentDelivre> & DocumentDelivreFormDefaults),
  ): DocumentDelivreFormRawValue | PartialWithRequiredKeyOf<NewDocumentDelivreFormRawValue> {
    return {
      ...documentDelivre,
      anneeDoc: documentDelivre.anneeDoc ? documentDelivre.anneeDoc.format(DATE_TIME_FORMAT) : undefined,
      dateEnregistrement: documentDelivre.dateEnregistrement ? documentDelivre.dateEnregistrement.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
