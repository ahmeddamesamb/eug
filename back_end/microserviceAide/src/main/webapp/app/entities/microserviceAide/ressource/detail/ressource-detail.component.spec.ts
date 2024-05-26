import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { RessourceDetailComponent } from './ressource-detail.component';

describe('Ressource Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RessourceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: RessourceDetailComponent,
              resolve: { ressource: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(RessourceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load ressource on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', RessourceDetailComponent);

      // THEN
      expect(instance.ressource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
