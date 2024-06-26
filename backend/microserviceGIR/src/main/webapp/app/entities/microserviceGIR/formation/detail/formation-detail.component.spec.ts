import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FormationDetailComponent } from './formation-detail.component';

describe('Formation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FormationDetailComponent,
              resolve: { formation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FormationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load formation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FormationDetailComponent);

      // THEN
      expect(instance.formation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
