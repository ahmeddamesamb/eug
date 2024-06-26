import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../mention.test-samples';

import { MentionFormService } from './mention-form.service';

describe('Mention Form Service', () => {
  let service: MentionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MentionFormService);
  });

  describe('Service methods', () => {
    describe('createMentionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMentionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleMention: expect.any(Object),
            actifYN: expect.any(Object),
            domaine: expect.any(Object),
          }),
        );
      });

      it('passing IMention should create a new form with FormGroup', () => {
        const formGroup = service.createMentionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            libelleMention: expect.any(Object),
            actifYN: expect.any(Object),
            domaine: expect.any(Object),
          }),
        );
      });
    });

    describe('getMention', () => {
      it('should return NewMention for default Mention initial value', () => {
        const formGroup = service.createMentionFormGroup(sampleWithNewData);

        const mention = service.getMention(formGroup) as any;

        expect(mention).toMatchObject(sampleWithNewData);
      });

      it('should return NewMention for empty Mention initial value', () => {
        const formGroup = service.createMentionFormGroup();

        const mention = service.getMention(formGroup) as any;

        expect(mention).toMatchObject({});
      });

      it('should return IMention', () => {
        const formGroup = service.createMentionFormGroup(sampleWithRequiredData);

        const mention = service.getMention(formGroup) as any;

        expect(mention).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMention should not enable id FormControl', () => {
        const formGroup = service.createMentionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMention should disable id FormControl', () => {
        const formGroup = service.createMentionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
