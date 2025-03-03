import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITypeDocument, NewTypeDocument } from '../type-document.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITypeDocument for edit and NewTypeDocumentFormGroupInput for create.
 */
type TypeDocumentFormGroupInput = ITypeDocument | PartialWithRequiredKeyOf<NewTypeDocument>;

type TypeDocumentFormDefaults = Pick<NewTypeDocument, 'id'>;

type TypeDocumentFormGroupContent = {
  id: FormControl<ITypeDocument['id'] | NewTypeDocument['id']>;
  libelleTypeDocument: FormControl<ITypeDocument['libelleTypeDocument']>;
};

export type TypeDocumentFormGroup = FormGroup<TypeDocumentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TypeDocumentFormService {
  createTypeDocumentFormGroup(typeDocument: TypeDocumentFormGroupInput = { id: null }): TypeDocumentFormGroup {
    const typeDocumentRawValue = {
      ...this.getFormDefaults(),
      ...typeDocument,
    };
    return new FormGroup<TypeDocumentFormGroupContent>({
      id: new FormControl(
        { value: typeDocumentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      libelleTypeDocument: new FormControl(typeDocumentRawValue.libelleTypeDocument),
    });
  }

  getTypeDocument(form: TypeDocumentFormGroup): ITypeDocument | NewTypeDocument {
    return form.getRawValue() as ITypeDocument | NewTypeDocument;
  }

  resetForm(form: TypeDocumentFormGroup, typeDocument: TypeDocumentFormGroupInput): void {
    const typeDocumentRawValue = { ...this.getFormDefaults(), ...typeDocument };
    form.reset(
      {
        ...typeDocumentRawValue,
        id: { value: typeDocumentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): TypeDocumentFormDefaults {
    return {
      id: null,
    };
  }
}
