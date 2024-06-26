import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PaysDetailComponent } from './pays-detail.component';

describe('Pays Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaysDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PaysDetailComponent,
              resolve: { pays: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PaysDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load pays on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PaysDetailComponent);

      // THEN
      expect(instance.pays).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
