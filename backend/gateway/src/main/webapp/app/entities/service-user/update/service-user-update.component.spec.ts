import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ServiceUserService } from '../service/service-user.service';
import { IServiceUser } from '../service-user.model';
import { ServiceUserFormService } from './service-user-form.service';

import { ServiceUserUpdateComponent } from './service-user-update.component';

describe('ServiceUser Management Update Component', () => {
  let comp: ServiceUserUpdateComponent;
  let fixture: ComponentFixture<ServiceUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let serviceUserFormService: ServiceUserFormService;
  let serviceUserService: ServiceUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ServiceUserUpdateComponent],
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
      .overrideTemplate(ServiceUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ServiceUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    serviceUserFormService = TestBed.inject(ServiceUserFormService);
    serviceUserService = TestBed.inject(ServiceUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const serviceUser: IServiceUser = { id: 456 };

      activatedRoute.data = of({ serviceUser });
      comp.ngOnInit();

      expect(comp.serviceUser).toEqual(serviceUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceUser>>();
      const serviceUser = { id: 123 };
      jest.spyOn(serviceUserFormService, 'getServiceUser').mockReturnValue(serviceUser);
      jest.spyOn(serviceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceUser }));
      saveSubject.complete();

      // THEN
      expect(serviceUserFormService.getServiceUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(serviceUserService.update).toHaveBeenCalledWith(expect.objectContaining(serviceUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceUser>>();
      const serviceUser = { id: 123 };
      jest.spyOn(serviceUserFormService, 'getServiceUser').mockReturnValue({ id: null });
      jest.spyOn(serviceUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: serviceUser }));
      saveSubject.complete();

      // THEN
      expect(serviceUserFormService.getServiceUser).toHaveBeenCalled();
      expect(serviceUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IServiceUser>>();
      const serviceUser = { id: 123 };
      jest.spyOn(serviceUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ serviceUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(serviceUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
