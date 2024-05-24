import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MentionDetailComponent } from './mention-detail.component';

describe('Mention Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MentionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MentionDetailComponent,
              resolve: { mention: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MentionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load mention on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MentionDetailComponent);

      // THEN
      expect(instance.mention).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
