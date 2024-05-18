import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NiveauDetailComponent } from './niveau-detail.component';

describe('Niveau Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NiveauDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: NiveauDetailComponent,
              resolve: { niveau: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(NiveauDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load niveau on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', NiveauDetailComponent);

      // THEN
      expect(instance.niveau).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
