import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUtilisateur, NewUtilisateur } from '../utilisateur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUtilisateur for edit and NewUtilisateurFormGroupInput for create.
 */
type UtilisateurFormGroupInput = IUtilisateur | PartialWithRequiredKeyOf<NewUtilisateur>;

type UtilisateurFormDefaults = Pick<NewUtilisateur, 'id'>;

type UtilisateurFormGroupContent = {
  id: FormControl<IUtilisateur['id'] | NewUtilisateur['id']>;
  nomUser: FormControl<IUtilisateur['nomUser']>;
  prenomUser: FormControl<IUtilisateur['prenomUser']>;
  emailUser: FormControl<IUtilisateur['emailUser']>;
  motDePasse: FormControl<IUtilisateur['motDePasse']>;
  role: FormControl<IUtilisateur['role']>;
  matricule: FormControl<IUtilisateur['matricule']>;
  departement: FormControl<IUtilisateur['departement']>;
  statut: FormControl<IUtilisateur['statut']>;
};

export type UtilisateurFormGroup = FormGroup<UtilisateurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UtilisateurFormService {
  createUtilisateurFormGroup(utilisateur: UtilisateurFormGroupInput = { id: null }): UtilisateurFormGroup {
    const utilisateurRawValue = {
      ...this.getFormDefaults(),
      ...utilisateur,
    };
    return new FormGroup<UtilisateurFormGroupContent>({
      id: new FormControl(
        { value: utilisateurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nomUser: new FormControl(utilisateurRawValue.nomUser),
      prenomUser: new FormControl(utilisateurRawValue.prenomUser),
      emailUser: new FormControl(utilisateurRawValue.emailUser),
      motDePasse: new FormControl(utilisateurRawValue.motDePasse),
      role: new FormControl(utilisateurRawValue.role),
      matricule: new FormControl(utilisateurRawValue.matricule),
      departement: new FormControl(utilisateurRawValue.departement),
      statut: new FormControl(utilisateurRawValue.statut),
    });
  }

  getUtilisateur(form: UtilisateurFormGroup): IUtilisateur | NewUtilisateur {
    return form.getRawValue() as IUtilisateur | NewUtilisateur;
  }

  resetForm(form: UtilisateurFormGroup, utilisateur: UtilisateurFormGroupInput): void {
    const utilisateurRawValue = { ...this.getFormDefaults(), ...utilisateur };
    form.reset(
      {
        ...utilisateurRawValue,
        id: { value: utilisateurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): UtilisateurFormDefaults {
    return {
      id: null,
    };
  }
}
