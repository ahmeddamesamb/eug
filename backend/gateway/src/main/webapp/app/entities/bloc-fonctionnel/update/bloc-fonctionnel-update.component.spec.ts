import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IServiceUser } from 'app/entities/service-user/service-user.model';
import { ServiceUserService } from 'app/entities/service-user/service/service-user.service';
import { BlocFonctionnelService } from '../service/bloc-fonctionnel.service';
import { IBlocFonctionnel } from '../bloc-fonctionnel.model';
import { BlocFonctionnelFormService } from './bloc-fonctionnel-form.service';

import { BlocFonctionnelUpdateComponent } from './bloc-fonctionnel-update.component';

describe('BlocFonctionnel Management Update Component', () => {
  let comp: BlocFonctionnelUpdateComponent;
  let fixture: ComponentFixture<BlocFonctionnelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let blocFonctionnelFormService: BlocFonctionnelFormService;
  let blocFonctionnelService: BlocFonctionnelService;
  let serviceUserService: ServiceUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), BlocFonctionnelUpdateComponent],
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
      .overrideTemplate(BlocFonctionnelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BlocFonctionnelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    blocFonctionnelFormService = TestBed.inject(BlocFonctionnelFormService);
    blocFonctionnelService = TestBed.inject(BlocFonctionnelService);
    serviceUserService = TestBed.inject(ServiceUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call ServiceUser query and add missing value', () => {
      const blocFonctionnel: IBlocFonctionnel = { id: 456 };
      const service: IServiceUser = { id: 31945 };
      blocFonctionnel.service = service;

      const serviceUserCollection: IServiceUser[] = [{ id: 31314 }];
      jest.spyOn(serviceUserService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceUserCollection })));
      const additionalServiceUsers = [service];
      const expectedCollection: IServiceUser[] = [...additionalServiceUsers, ...serviceUserCollection];
      jest.spyOn(serviceUserService, 'addServiceUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ blocFonctionnel });
      comp.ngOnInit();

      expect(serviceUserService.query).toHaveBeenCalled();
      expect(serviceUserService.addServiceUserToCollectionIfMissing).toHaveBeenCalledWith(
        serviceUserCollection,
        ...additionalServiceUsers.map(expect.objectContaining),
      );
      expect(comp.serviceUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const blocFonctionnel: IBlocFonctionnel = { id: 456 };
      const service: IServiceUser = { id: 15788 };
      blocFonctionnel.service = service;

      activatedRoute.data = of({ blocFonctionnel });
      comp.ngOnInit();

      expect(comp.serviceUsersSharedCollection).toContain(service);
      expect(comp.blocFonctionnel).toEqual(blocFonctionnel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlocFonctionnel>>();
      const blocFonctionnel = { id: 123 };
      jest.spyOn(blocFonctionnelFormService, 'getBlocFonctionnel').mockReturnValue(blocFonctionnel);
      jest.spyOn(blocFonctionnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blocFonctionnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blocFonctionnel }));
      saveSubject.complete();

      // THEN
      expect(blocFonctionnelFormService.getBlocFonctionnel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(blocFonctionnelService.update).toHaveBeenCalledWith(expect.objectContaining(blocFonctionnel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlocFonctionnel>>();
      const blocFonctionnel = { id: 123 };
      jest.spyOn(blocFonctionnelFormService, 'getBlocFonctionnel').mockReturnValue({ id: null });
      jest.spyOn(blocFonctionnelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blocFonctionnel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: blocFonctionnel }));
      saveSubject.complete();

      // THEN
      expect(blocFonctionnelFormService.getBlocFonctionnel).toHaveBeenCalled();
      expect(blocFonctionnelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBlocFonctionnel>>();
      const blocFonctionnel = { id: 123 };
      jest.spyOn(blocFonctionnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ blocFonctionnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(blocFonctionnelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareServiceUser', () => {
      it('Should forward to serviceUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(serviceUserService, 'compareServiceUser');
        comp.compareServiceUser(entity, entity2);
        expect(serviceUserService.compareServiceUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
