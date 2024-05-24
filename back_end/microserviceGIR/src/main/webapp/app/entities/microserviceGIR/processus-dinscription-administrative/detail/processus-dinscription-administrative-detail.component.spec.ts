import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProcessusDinscriptionAdministrativeDetailComponent } from './processus-dinscription-administrative-detail.component';

describe('ProcessusDinscriptionAdministrative Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProcessusDinscriptionAdministrativeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProcessusDinscriptionAdministrativeDetailComponent,
              resolve: { processusDinscriptionAdministrative: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProcessusDinscriptionAdministrativeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load processusDinscriptionAdministrative on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProcessusDinscriptionAdministrativeDetailComponent);

      // THEN
      expect(instance.processusDinscriptionAdministrative).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
