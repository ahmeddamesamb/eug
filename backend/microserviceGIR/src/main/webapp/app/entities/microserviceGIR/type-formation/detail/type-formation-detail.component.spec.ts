import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TypeFormationDetailComponent } from './type-formation-detail.component';

describe('TypeFormation Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeFormationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TypeFormationDetailComponent,
              resolve: { typeFormation: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TypeFormationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load typeFormation on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TypeFormationDetailComponent);

      // THEN
      expect(instance.typeFormation).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
