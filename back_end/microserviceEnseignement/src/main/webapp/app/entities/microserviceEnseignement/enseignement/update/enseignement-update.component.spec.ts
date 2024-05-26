import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EnseignementService } from '../service/enseignement.service';
import { IEnseignement } from '../enseignement.model';
import { EnseignementFormService } from './enseignement-form.service';

import { EnseignementUpdateComponent } from './enseignement-update.component';

describe('Enseignement Management Update Component', () => {
  let comp: EnseignementUpdateComponent;
  let fixture: ComponentFixture<EnseignementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let enseignementFormService: EnseignementFormService;
  let enseignementService: EnseignementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), EnseignementUpdateComponent],
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
      .overrideTemplate(EnseignementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EnseignementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    enseignementFormService = TestBed.inject(EnseignementFormService);
    enseignementService = TestBed.inject(EnseignementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const enseignement: IEnseignement = { id: 456 };

      activatedRoute.data = of({ enseignement });
      comp.ngOnInit();

      expect(comp.enseignement).toEqual(enseignement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignement>>();
      const enseignement = { id: 123 };
      jest.spyOn(enseignementFormService, 'getEnseignement').mockReturnValue(enseignement);
      jest.spyOn(enseignementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enseignement }));
      saveSubject.complete();

      // THEN
      expect(enseignementFormService.getEnseignement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(enseignementService.update).toHaveBeenCalledWith(expect.objectContaining(enseignement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignement>>();
      const enseignement = { id: 123 };
      jest.spyOn(enseignementFormService, 'getEnseignement').mockReturnValue({ id: null });
      jest.spyOn(enseignementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: enseignement }));
      saveSubject.complete();

      // THEN
      expect(enseignementFormService.getEnseignement).toHaveBeenCalled();
      expect(enseignementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEnseignement>>();
      const enseignement = { id: 123 };
      jest.spyOn(enseignementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ enseignement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(enseignementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
