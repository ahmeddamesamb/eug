import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InscriptionAdministrativeFormationDetailComponent } from './inscription-administrative-formation-detail.component';

describe('InscriptionAdministrativeFormation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionAdministrativeFormationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InscriptionAdministrativeFormationDetailComponent,
              resolve: { inscriptionAdministrativeFormation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InscriptionAdministrativeFormationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load inscriptionAdministrativeFormation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InscriptionAdministrativeFormationDetailComponent);

      // THEN
      expect(instance.inscriptionAdministrativeFormation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
