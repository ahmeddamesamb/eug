import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { IRessource } from 'app/entities/ressource/ressource.model';
import { RessourceService } from 'app/entities/ressource/service/ressource.service';
import { IInfoUserRessource } from '../info-user-ressource.model';
import { InfoUserRessourceService } from '../service/info-user-ressource.service';
import { InfoUserRessourceFormService } from './info-user-ressource-form.service';

import { InfoUserRessourceUpdateComponent } from './info-user-ressource-update.component';

describe('InfoUserRessource Management Update Component', () => {
  let comp: InfoUserRessourceUpdateComponent;
  let fixture: ComponentFixture<InfoUserRessourceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infoUserRessourceFormService: InfoUserRessourceFormService;
  let infoUserRessourceService: InfoUserRessourceService;
  let infosUserService: InfosUserService;
  let ressourceService: RessourceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InfoUserRessourceUpdateComponent],
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
      .overrideTemplate(InfoUserRessourceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfoUserRessourceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infoUserRessourceFormService = TestBed.inject(InfoUserRessourceFormService);
    infoUserRessourceService = TestBed.inject(InfoUserRessourceService);
    infosUserService = TestBed.inject(InfosUserService);
    ressourceService = TestBed.inject(RessourceService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfosUser query and add missing value', () => {
      const infoUserRessource: IInfoUserRessource = { id: 456 };
      const infosUser: IInfosUser = { id: 31945 };
      infoUserRessource.infosUser = infosUser;

      const infosUserCollection: IInfosUser[] = [{ id: 9301 }];
      jest.spyOn(infosUserService, 'query').mockReturnValue(of(new HttpResponse({ body: infosUserCollection })));
      const additionalInfosUsers = [infosUser];
      const expectedCollection: IInfosUser[] = [...additionalInfosUsers, ...infosUserCollection];
      jest.spyOn(infosUserService, 'addInfosUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infoUserRessource });
      comp.ngOnInit();

      expect(infosUserService.query).toHaveBeenCalled();
      expect(infosUserService.addInfosUserToCollectionIfMissing).toHaveBeenCalledWith(
        infosUserCollection,
        ...additionalInfosUsers.map(expect.objectContaining),
      );
      expect(comp.infosUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ressource query and add missing value', () => {
      const infoUserRessource: IInfoUserRessource = { id: 456 };
      const ressource: IRessource = { id: 388 };
      infoUserRessource.ressource = ressource;

      const ressourceCollection: IRessource[] = [{ id: 1399 }];
      jest.spyOn(ressourceService, 'query').mockReturnValue(of(new HttpResponse({ body: ressourceCollection })));
      const additionalRessources = [ressource];
      const expectedCollection: IRessource[] = [...additionalRessources, ...ressourceCollection];
      jest.spyOn(ressourceService, 'addRessourceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infoUserRessource });
      comp.ngOnInit();

      expect(ressourceService.query).toHaveBeenCalled();
      expect(ressourceService.addRessourceToCollectionIfMissing).toHaveBeenCalledWith(
        ressourceCollection,
        ...additionalRessources.map(expect.objectContaining),
      );
      expect(comp.ressourcesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infoUserRessource: IInfoUserRessource = { id: 456 };
      const infosUser: IInfosUser = { id: 30802 };
      infoUserRessource.infosUser = infosUser;
      const ressource: IRessource = { id: 32417 };
      infoUserRessource.ressource = ressource;

      activatedRoute.data = of({ infoUserRessource });
      comp.ngOnInit();

      expect(comp.infosUsersSharedCollection).toContain(infosUser);
      expect(comp.ressourcesSharedCollection).toContain(ressource);
      expect(comp.infoUserRessource).toEqual(infoUserRessource);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfoUserRessource>>();
      const infoUserRessource = { id: 123 };
      jest.spyOn(infoUserRessourceFormService, 'getInfoUserRessource').mockReturnValue(infoUserRessource);
      jest.spyOn(infoUserRessourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoUserRessource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infoUserRessource }));
      saveSubject.complete();

      // THEN
      expect(infoUserRessourceFormService.getInfoUserRessource).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(infoUserRessourceService.update).toHaveBeenCalledWith(expect.objectContaining(infoUserRessource));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfoUserRessource>>();
      const infoUserRessource = { id: 123 };
      jest.spyOn(infoUserRessourceFormService, 'getInfoUserRessource').mockReturnValue({ id: null });
      jest.spyOn(infoUserRessourceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoUserRessource: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infoUserRessource }));
      saveSubject.complete();

      // THEN
      expect(infoUserRessourceFormService.getInfoUserRessource).toHaveBeenCalled();
      expect(infoUserRessourceService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfoUserRessource>>();
      const infoUserRessource = { id: 123 };
      jest.spyOn(infoUserRessourceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infoUserRessource });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infoUserRessourceService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareInfosUser', () => {
      it('Should forward to infosUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(infosUserService, 'compareInfosUser');
        comp.compareInfosUser(entity, entity2);
        expect(infosUserService.compareInfosUser).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRessource', () => {
      it('Should forward to ressourceService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ressourceService, 'compareRessource');
        comp.compareRessource(entity, entity2);
        expect(ressourceService.compareRessource).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
