import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { InfosUserDetailComponent } from './infos-user-detail.component';

describe('InfosUser Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfosUserDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: InfosUserDetailComponent,
              resolve: { infosUser: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(InfosUserDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load infosUser on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', InfosUserDetailComponent);

      // THEN
      expect(instance.infosUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
