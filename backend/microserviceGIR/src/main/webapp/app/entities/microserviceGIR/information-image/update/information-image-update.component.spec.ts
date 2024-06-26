import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { InformationImageService } from '../service/information-image.service';
import { IInformationImage } from '../information-image.model';
import { InformationImageFormService } from './information-image-form.service';

import { InformationImageUpdateComponent } from './information-image-update.component';

describe('InformationImage Management Update Component', () => {
  let comp: InformationImageUpdateComponent;
  let fixture: ComponentFixture<InformationImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let informationImageFormService: InformationImageFormService;
  let informationImageService: InformationImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InformationImageUpdateComponent],
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
      .overrideTemplate(InformationImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InformationImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    informationImageFormService = TestBed.inject(InformationImageFormService);
    informationImageService = TestBed.inject(InformationImageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const informationImage: IInformationImage = { id: 456 };

      activatedRoute.data = of({ informationImage });
      comp.ngOnInit();

      expect(comp.informationImage).toEqual(informationImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationImage>>();
      const informationImage = { id: 123 };
      jest.spyOn(informationImageFormService, 'getInformationImage').mockReturnValue(informationImage);
      jest.spyOn(informationImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informationImage }));
      saveSubject.complete();

      // THEN
      expect(informationImageFormService.getInformationImage).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(informationImageService.update).toHaveBeenCalledWith(expect.objectContaining(informationImage));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationImage>>();
      const informationImage = { id: 123 };
      jest.spyOn(informationImageFormService, 'getInformationImage').mockReturnValue({ id: null });
      jest.spyOn(informationImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationImage: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: informationImage }));
      saveSubject.complete();

      // THEN
      expect(informationImageFormService.getInformationImage).toHaveBeenCalled();
      expect(informationImageService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInformationImage>>();
      const informationImage = { id: 123 };
      jest.spyOn(informationImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ informationImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(informationImageService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
