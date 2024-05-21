import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DoctoratDetailComponent } from './doctorat-detail.component';

describe('Doctorat Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DoctoratDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DoctoratDetailComponent,
              resolve: { doctorat: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DoctoratDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load doctorat on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DoctoratDetailComponent);

      // THEN
      expect(instance.doctorat).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
