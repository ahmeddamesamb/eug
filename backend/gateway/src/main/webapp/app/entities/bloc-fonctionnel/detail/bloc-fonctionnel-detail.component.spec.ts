import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BlocFonctionnelDetailComponent } from './bloc-fonctionnel-detail.component';

describe('BlocFonctionnel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BlocFonctionnelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BlocFonctionnelDetailComponent,
              resolve: { blocFonctionnel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(BlocFonctionnelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load blocFonctionnel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BlocFonctionnelDetailComponent);

      // THEN
      expect(instance.blocFonctionnel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
