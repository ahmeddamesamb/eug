import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { LyceeDetailComponent } from './lycee-detail.component';

describe('Lycee Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LyceeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: LyceeDetailComponent,
              resolve: { lycee: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(LyceeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load lycee on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', LyceeDetailComponent);

      // THEN
      expect(instance.lycee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
