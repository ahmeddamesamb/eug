import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InformationPersonnelleDetailComponent } from './information-personnelle-detail.component';

describe('InformationPersonnelle Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformationPersonnelleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InformationPersonnelleDetailComponent,
              resolve: { informationPersonnelle: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InformationPersonnelleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load informationPersonnelle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InformationPersonnelleDetailComponent);

      // THEN
      expect(instance.informationPersonnelle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
