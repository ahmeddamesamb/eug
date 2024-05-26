import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AnneeAcademiqueService } from '../service/annee-academique.service';
import { IAnneeAcademique } from '../annee-academique.model';
import { AnneeAcademiqueFormService } from './annee-academique-form.service';

import { AnneeAcademiqueUpdateComponent } from './annee-academique-update.component';

describe('AnneeAcademique Management Update Component', () => {
  let comp: AnneeAcademiqueUpdateComponent;
  let fixture: ComponentFixture<AnneeAcademiqueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let anneeAcademiqueFormService: AnneeAcademiqueFormService;
  let anneeAcademiqueService: AnneeAcademiqueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AnneeAcademiqueUpdateComponent],
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
      .overrideTemplate(AnneeAcademiqueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnneeAcademiqueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    anneeAcademiqueFormService = TestBed.inject(AnneeAcademiqueFormService);
    anneeAcademiqueService = TestBed.inject(AnneeAcademiqueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const anneeAcademique: IAnneeAcademique = { id: 456 };

      activatedRoute.data = of({ anneeAcademique });
      comp.ngOnInit();

      expect(comp.anneeAcademique).toEqual(anneeAcademique);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnneeAcademique>>();
      const anneeAcademique = { id: 123 };
      jest.spyOn(anneeAcademiqueFormService, 'getAnneeAcademique').mockReturnValue(anneeAcademique);
      jest.spyOn(anneeAcademiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anneeAcademique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anneeAcademique }));
      saveSubject.complete();

      // THEN
      expect(anneeAcademiqueFormService.getAnneeAcademique).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(anneeAcademiqueService.update).toHaveBeenCalledWith(expect.objectContaining(anneeAcademique));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnneeAcademique>>();
      const anneeAcademique = { id: 123 };
      jest.spyOn(anneeAcademiqueFormService, 'getAnneeAcademique').mockReturnValue({ id: null });
      jest.spyOn(anneeAcademiqueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anneeAcademique: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: anneeAcademique }));
      saveSubject.complete();

      // THEN
      expect(anneeAcademiqueFormService.getAnneeAcademique).toHaveBeenCalled();
      expect(anneeAcademiqueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnneeAcademique>>();
      const anneeAcademique = { id: 123 };
      jest.spyOn(anneeAcademiqueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ anneeAcademique });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(anneeAcademiqueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
