import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UniversiteDetailComponent } from './universite-detail.component';

describe('Universite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UniversiteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UniversiteDetailComponent,
              resolve: { universite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UniversiteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load universite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UniversiteDetailComponent);

      // THEN
      expect(instance.universite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
