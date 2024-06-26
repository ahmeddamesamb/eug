import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeSelectionDetailComponent } from './type-selection-detail.component';

describe('TypeSelection Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeSelectionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeSelectionDetailComponent,
              resolve: { typeSelection: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeSelectionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeSelection on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeSelectionDetailComponent);

      // THEN
      expect(instance.typeSelection).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
