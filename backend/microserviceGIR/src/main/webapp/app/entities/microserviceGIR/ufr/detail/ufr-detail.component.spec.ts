import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UfrDetailComponent } from './ufr-detail.component';

describe('Ufr Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UfrDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UfrDetailComponent,
              resolve: { ufr: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UfrDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ufr on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UfrDetailComponent);

      // THEN
      expect(instance.ufr).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
