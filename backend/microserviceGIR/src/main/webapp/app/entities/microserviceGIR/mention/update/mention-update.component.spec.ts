import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDomaine } from 'app/entities/microserviceGIR/domaine/domaine.model';
import { DomaineService } from 'app/entities/microserviceGIR/domaine/service/domaine.service';
import { MentionService } from '../service/mention.service';
import { IMention } from '../mention.model';
import { MentionFormService } from './mention-form.service';

import { MentionUpdateComponent } from './mention-update.component';

describe('Mention Management Update Component', () => {
  let comp: MentionUpdateComponent;
  let fixture: ComponentFixture<MentionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mentionFormService: MentionFormService;
  let mentionService: MentionService;
  let domaineService: DomaineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MentionUpdateComponent],
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
      .overrideTemplate(MentionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MentionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mentionFormService = TestBed.inject(MentionFormService);
    mentionService = TestBed.inject(MentionService);
    domaineService = TestBed.inject(DomaineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Domaine query and add missing value', () => {
      const mention: IMention = { id: 456 };
      const domaine: IDomaine = { id: 12024 };
      mention.domaine = domaine;

      const domaineCollection: IDomaine[] = [{ id: 12242 }];
      jest.spyOn(domaineService, 'query').mockReturnValue(of(new HttpResponse({ body: domaineCollection })));
      const additionalDomaines = [domaine];
      const expectedCollection: IDomaine[] = [...additionalDomaines, ...domaineCollection];
      jest.spyOn(domaineService, 'addDomaineToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ mention });
      comp.ngOnInit();

      expect(domaineService.query).toHaveBeenCalled();
      expect(domaineService.addDomaineToCollectionIfMissing).toHaveBeenCalledWith(
        domaineCollection,
        ...additionalDomaines.map(expect.objectContaining),
      );
      expect(comp.domainesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const mention: IMention = { id: 456 };
      const domaine: IDomaine = { id: 888 };
      mention.domaine = domaine;

      activatedRoute.data = of({ mention });
      comp.ngOnInit();

      expect(comp.domainesSharedCollection).toContain(domaine);
      expect(comp.mention).toEqual(mention);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMention>>();
      const mention = { id: 123 };
      jest.spyOn(mentionFormService, 'getMention').mockReturnValue(mention);
      jest.spyOn(mentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mention }));
      saveSubject.complete();

      // THEN
      expect(mentionFormService.getMention).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mentionService.update).toHaveBeenCalledWith(expect.objectContaining(mention));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMention>>();
      const mention = { id: 123 };
      jest.spyOn(mentionFormService, 'getMention').mockReturnValue({ id: null });
      jest.spyOn(mentionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mention: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: mention }));
      saveSubject.complete();

      // THEN
      expect(mentionFormService.getMention).toHaveBeenCalled();
      expect(mentionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMention>>();
      const mention = { id: 123 };
      jest.spyOn(mentionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ mention });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mentionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDomaine', () => {
      it('Should forward to domaineService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(domaineService, 'compareDomaine');
        comp.compareDomaine(entity, entity2);
        expect(domaineService.compareDomaine).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
