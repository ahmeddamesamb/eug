import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MinistereService } from '../service/ministere.service';
import { IMinistere } from '../ministere.model';
import { MinistereFormService } from './ministere-form.service';

import { MinistereUpdateComponent } from './ministere-update.component';

describe('Ministere Management Update Component', () => {
  let comp: MinistereUpdateComponent;
  let fixture: ComponentFixture<MinistereUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ministereFormService: MinistereFormService;
  let ministereService: MinistereService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MinistereUpdateComponent],
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
      .overrideTemplate(MinistereUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MinistereUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ministereFormService = TestBed.inject(MinistereFormService);
    ministereService = TestBed.inject(MinistereService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ministere: IMinistere = { id: 456 };

      activatedRoute.data = of({ ministere });
      comp.ngOnInit();

      expect(comp.ministere).toEqual(ministere);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistere>>();
      const ministere = { id: 123 };
      jest.spyOn(ministereFormService, 'getMinistere').mockReturnValue(ministere);
      jest.spyOn(ministereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministere }));
      saveSubject.complete();

      // THEN
      expect(ministereFormService.getMinistere).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ministereService.update).toHaveBeenCalledWith(expect.objectContaining(ministere));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistere>>();
      const ministere = { id: 123 };
      jest.spyOn(ministereFormService, 'getMinistere').mockReturnValue({ id: null });
      jest.spyOn(ministereService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministere: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ministere }));
      saveSubject.complete();

      // THEN
      expect(ministereFormService.getMinistere).toHaveBeenCalled();
      expect(ministereService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMinistere>>();
      const ministere = { id: 123 };
      jest.spyOn(ministereService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ministere });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ministereService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
