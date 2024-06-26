import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMention } from 'app/entities/microserviceGIR/mention/mention.model';
import { MentionService } from 'app/entities/microserviceGIR/mention/service/mention.service';
import { SpecialiteService } from '../service/specialite.service';
import { ISpecialite } from '../specialite.model';
import { SpecialiteFormService } from './specialite-form.service';

import { SpecialiteUpdateComponent } from './specialite-update.component';

describe('Specialite Management Update Component', () => {
  let comp: SpecialiteUpdateComponent;
  let fixture: ComponentFixture<SpecialiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let specialiteFormService: SpecialiteFormService;
  let specialiteService: SpecialiteService;
  let mentionService: MentionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SpecialiteUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SpecialiteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SpecialiteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    specialiteFormService = TestBed.inject(SpecialiteFormService);
    specialiteService = TestBed.inject(SpecialiteService);
    mentionService = TestBed.inject(MentionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Mention query and add missing value', () => {
      const specialite: ISpecialite = { id: 456 };
      const mention: IMention = { id: 20531 };
      specialite.mention = mention;

      const mentionCollection: IMention[] = [{ id: 16337 }];
      jest.spyOn(mentionService, 'query').mockReturnValue(of(new HttpResponse({ body: mentionCollection })));
      const additionalMentions = [mention];
      const expectedCollection: IMention[] = [...additionalMentions, ...mentionCollection];
      jest.spyOn(mentionService, 'addMentionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ specialite });
      comp.ngOnInit();

      expect(mentionService.query).toHaveBeenCalled();
      expect(mentionService.addMentionToCollectionIfMissing).toHaveBeenCalledWith(
        mentionCollection,
        ...additionalMentions.map(expect.objectContaining),
      );
      expect(comp.mentionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const specialite: ISpecialite = { id: 456 };
      const mention: IMention = { id: 28946 };
      specialite.mention = mention;

      activatedRoute.data = of({ specialite });
      comp.ngOnInit();

      expect(comp.mentionsSharedCollection).toContain(mention);
      expect(comp.specialite).toEqual(specialite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialite>>();
      const specialite = { id: 123 };
      jest.spyOn(specialiteFormService, 'getSpecialite').mockReturnValue(specialite);
      jest.spyOn(specialiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialite }));
      saveSubject.complete();

      // THEN
      expect(specialiteFormService.getSpecialite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(specialiteService.update).toHaveBeenCalledWith(expect.objectContaining(specialite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialite>>();
      const specialite = { id: 123 };
      jest.spyOn(specialiteFormService, 'getSpecialite').mockReturnValue({ id: null });
      jest.spyOn(specialiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: specialite }));
      saveSubject.complete();

      // THEN
      expect(specialiteFormService.getSpecialite).toHaveBeenCalled();
      expect(specialiteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISpecialite>>();
      const specialite = { id: 123 };
      jest.spyOn(specialiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ specialite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(specialiteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMention', () => {
      it('Should forward to mentionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mentionService, 'compareMention');
        comp.compareMention(entity, entity2);
        expect(mentionService.compareMention).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
