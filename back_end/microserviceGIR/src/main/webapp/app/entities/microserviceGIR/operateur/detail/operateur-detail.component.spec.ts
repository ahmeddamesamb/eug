import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OperateurDetailComponent } from './operateur-detail.component';

describe('Operateur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OperateurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OperateurDetailComponent,
              resolve: { operateur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(OperateurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load operateur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OperateurDetailComponent);

      // THEN
      expect(instance.operateur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
