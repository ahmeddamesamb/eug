import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InscriptionAdministrativeDetailComponent } from './inscription-administrative-detail.component';

describe('InscriptionAdministrative Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionAdministrativeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InscriptionAdministrativeDetailComponent,
              resolve: { inscriptionAdministrative: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InscriptionAdministrativeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load inscriptionAdministrative on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InscriptionAdministrativeDetailComponent);

      // THEN
      expect(instance.inscriptionAdministrative).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
