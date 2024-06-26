import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserProfileBlocFonctionnelDetailComponent } from './user-profile-bloc-fonctionnel-detail.component';

describe('UserProfileBlocFonctionnel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserProfileBlocFonctionnelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserProfileBlocFonctionnelDetailComponent,
              resolve: { userProfileBlocFonctionnel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(UserProfileBlocFonctionnelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load userProfileBlocFonctionnel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserProfileBlocFonctionnelDetailComponent);

      // THEN
      expect(instance.userProfileBlocFonctionnel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
