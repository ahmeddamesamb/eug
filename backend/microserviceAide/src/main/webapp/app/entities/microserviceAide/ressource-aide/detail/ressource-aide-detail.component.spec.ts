import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RessourceAideDetailComponent } from './ressource-aide-detail.component';

describe('RessourceAide Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RessourceAideDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RessourceAideDetailComponent,
              resolve: { ressourceAide: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RessourceAideDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ressourceAide on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RessourceAideDetailComponent);

      // THEN
      expect(instance.ressourceAide).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
