import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DomaineService } from '../service/domaine.service';
import { IDomaine } from '../domaine.model';
import { DomaineFormService } from './domaine-form.service';

import { DomaineUpdateComponent } from './domaine-update.component';

describe('Domaine Management Update Component', () => {
  let comp: DomaineUpdateComponent;
  let fixture: ComponentFixture<DomaineUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let domaineFormService: DomaineFormService;
  let domaineService: DomaineService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DomaineUpdateComponent],
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
      .overrideTemplate(DomaineUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DomaineUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    domaineFormService = TestBed.inject(DomaineFormService);
    domaineService = TestBed.inject(DomaineService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const domaine: IDomaine = { id: 456 };

      activatedRoute.data = of({ domaine });
      comp.ngOnInit();

      expect(comp.domaine).toEqual(domaine);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaine>>();
      const domaine = { id: 123 };
      jest.spyOn(domaineFormService, 'getDomaine').mockReturnValue(domaine);
      jest.spyOn(domaineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaine }));
      saveSubject.complete();

      // THEN
      expect(domaineFormService.getDomaine).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(domaineService.update).toHaveBeenCalledWith(expect.objectContaining(domaine));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaine>>();
      const domaine = { id: 123 };
      jest.spyOn(domaineFormService, 'getDomaine').mockReturnValue({ id: null });
      jest.spyOn(domaineService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaine: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: domaine }));
      saveSubject.complete();

      // THEN
      expect(domaineFormService.getDomaine).toHaveBeenCalled();
      expect(domaineService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDomaine>>();
      const domaine = { id: 123 };
      jest.spyOn(domaineService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ domaine });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(domaineService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
