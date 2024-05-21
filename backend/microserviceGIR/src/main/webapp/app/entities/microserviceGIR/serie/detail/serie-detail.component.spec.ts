import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SerieDetailComponent } from './serie-detail.component';

describe('Serie Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SerieDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SerieDetailComponent,
              resolve: { serie: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SerieDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load serie on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SerieDetailComponent);

      // THEN
      expect(instance.serie).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
