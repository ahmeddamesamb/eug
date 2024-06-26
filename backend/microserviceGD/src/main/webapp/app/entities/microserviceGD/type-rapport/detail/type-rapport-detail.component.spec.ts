import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeRapportDetailComponent } from './type-rapport-detail.component';

describe('TypeRapport Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeRapportDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeRapportDetailComponent,
              resolve: { typeRapport: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeRapportDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeRapport on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeRapportDetailComponent);

      // THEN
      expect(instance.typeRapport).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
