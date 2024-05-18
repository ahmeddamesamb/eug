import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { FraisDetailComponent } from './frais-detail.component';

describe('Frais Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FraisDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: FraisDetailComponent,
              resolve: { frais: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(FraisDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load frais on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', FraisDetailComponent);

      // THEN
      expect(instance.frais).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
