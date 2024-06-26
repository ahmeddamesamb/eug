import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcessusInscriptionAdministrativeDetailComponent } from './processus-inscription-administrative-detail.component';

describe('ProcessusInscriptionAdministrative Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessusInscriptionAdministrativeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProcessusInscriptionAdministrativeDetailComponent,
              resolve: { processusInscriptionAdministrative: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProcessusInscriptionAdministrativeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load processusInscriptionAdministrative on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProcessusInscriptionAdministrativeDetailComponent);

      // THEN
      expect(instance.processusInscriptionAdministrative).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
