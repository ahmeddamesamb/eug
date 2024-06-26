import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InfoUserRessourceDetailComponent } from './info-user-ressource-detail.component';

describe('InfoUserRessource Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfoUserRessourceDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InfoUserRessourceDetailComponent,
              resolve: { infoUserRessource: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InfoUserRessourceDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load infoUserRessource on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InfoUserRessourceDetailComponent);

      // THEN
      expect(instance.infoUserRessource).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
