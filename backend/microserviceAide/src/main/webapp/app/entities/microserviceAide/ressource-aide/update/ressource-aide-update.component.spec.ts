import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { RessourceAideService } from '../service/ressource-aide.service';
import { IRessourceAide } from '../ressource-aide.model';
import { RessourceAideFormService } from './ressource-aide-form.service';

import { RessourceAideUpdateComponent } from './ressource-aide-update.component';

describe('RessourceAide Management Update Component', () => {
  let comp: RessourceAideUpdateComponent;
  let fixture: ComponentFixture<RessourceAideUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ressourceAideFormService: RessourceAideFormService;
  let ressourceAideService: RessourceAideService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), RessourceAideUpdateComponent],
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
      .overrideTemplate(RessourceAideUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RessourceAideUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ressourceAideFormService = TestBed.inject(RessourceAideFormService);
    ressourceAideService = TestBed.inject(RessourceAideService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ressourceAide: IRessourceAide = { id: 456 };

      activatedRoute.data = of({ ressourceAide });
      comp.ngOnInit();

      expect(comp.ressourceAide).toEqual(ressourceAide);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRessourceAide>>();
      const ressourceAide = { id: 123 };
      jest.spyOn(ressourceAideFormService, 'getRessourceAide').mockReturnValue(ressourceAide);
      jest.spyOn(ressourceAideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressourceAide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ressourceAide }));
      saveSubject.complete();

      // THEN
      expect(ressourceAideFormService.getRessourceAide).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ressourceAideService.update).toHaveBeenCalledWith(expect.objectContaining(ressourceAide));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRessourceAide>>();
      const ressourceAide = { id: 123 };
      jest.spyOn(ressourceAideFormService, 'getRessourceAide').mockReturnValue({ id: null });
      jest.spyOn(ressourceAideService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressourceAide: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ressourceAide }));
      saveSubject.complete();

      // THEN
      expect(ressourceAideFormService.getRessourceAide).toHaveBeenCalled();
      expect(ressourceAideService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IRessourceAide>>();
      const ressourceAide = { id: 123 };
      jest.spyOn(ressourceAideService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ressourceAide });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ressourceAideService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
