import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProfilDetailComponent } from './profil-detail.component';

describe('Profil Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfilDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProfilDetailComponent,
              resolve: { profil: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(ProfilDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load profil on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProfilDetailComponent);

      // THEN
      expect(instance.profil).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
