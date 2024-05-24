import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProgrammationInscriptionDetailComponent } from './programmation-inscription-detail.component';

describe('ProgrammationInscription Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProgrammationInscriptionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProgrammationInscriptionDetailComponent,
              resolve: { programmationInscription: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProgrammationInscriptionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load programmationInscription on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProgrammationInscriptionDetailComponent);

      // THEN
      expect(instance.programmationInscription).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
