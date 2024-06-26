import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CampagneService } from '../service/campagne.service';
import { ICampagne } from '../campagne.model';
import { CampagneFormService } from './campagne-form.service';

import { CampagneUpdateComponent } from './campagne-update.component';

describe('Campagne Management Update Component', () => {
  let comp: CampagneUpdateComponent;
  let fixture: ComponentFixture<CampagneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let campagneFormService: CampagneFormService;
  let campagneService: CampagneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CampagneUpdateComponent],
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
      .overrideTemplate(CampagneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CampagneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    campagneFormService = TestBed.inject(CampagneFormService);
    campagneService = TestBed.inject(CampagneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const campagne: ICampagne = { id: 456 };

      activatedRoute.data = of({ campagne });
      comp.ngOnInit();

      expect(comp.campagne).toEqual(campagne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampagne>>();
      const campagne = { id: 123 };
      jest.spyOn(campagneFormService, 'getCampagne').mockReturnValue(campagne);
      jest.spyOn(campagneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campagne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campagne }));
      saveSubject.complete();

      // THEN
      expect(campagneFormService.getCampagne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(campagneService.update).toHaveBeenCalledWith(expect.objectContaining(campagne));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampagne>>();
      const campagne = { id: 123 };
      jest.spyOn(campagneFormService, 'getCampagne').mockReturnValue({ id: null });
      jest.spyOn(campagneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campagne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: campagne }));
      saveSubject.complete();

      // THEN
      expect(campagneFormService.getCampagne).toHaveBeenCalled();
      expect(campagneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICampagne>>();
      const campagne = { id: 123 };
      jest.spyOn(campagneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ campagne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(campagneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
