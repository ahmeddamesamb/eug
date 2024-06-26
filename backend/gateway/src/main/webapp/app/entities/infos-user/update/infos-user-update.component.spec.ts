import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { InfosUserService } from '../service/infos-user.service';
import { IInfosUser } from '../infos-user.model';

import { InfosUserFormService } from './infos-user-form.service';

import { InfosUserUpdateComponent } from './infos-user-update.component';

describe('InfosUser Management Update Component', () => {
  let comp: InfosUserUpdateComponent;
  let fixture: ComponentFixture<InfosUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let infosUserFormService: InfosUserFormService;
  let infosUserService: InfosUserService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), InfosUserUpdateComponent],
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
      .overrideTemplate(InfosUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(InfosUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    infosUserFormService = TestBed.inject(InfosUserFormService);
    infosUserService = TestBed.inject(InfosUserService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const infosUser: IInfosUser = { id: 456 };
      const user: IUser = { id: '3b81517e-40d8-477a-8937-935628c76962' };
      infosUser.user = user;

      const userCollection: IUser[] = [{ id: '74d8d0e7-5cc2-4731-b1f4-580ea7b64ce2' }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ infosUser });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const infosUser: IInfosUser = { id: 456 };
      const user: IUser = { id: '503440c1-1d16-49a9-9bed-89718e376649' };
      infosUser.user = user;

      activatedRoute.data = of({ infosUser });
      comp.ngOnInit();

      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.infosUser).toEqual(infosUser);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfosUser>>();
      const infosUser = { id: 123 };
      jest.spyOn(infosUserFormService, 'getInfosUser').mockReturnValue(infosUser);
      jest.spyOn(infosUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infosUser }));
      saveSubject.complete();

      // THEN
      expect(infosUserFormService.getInfosUser).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(infosUserService.update).toHaveBeenCalledWith(expect.objectContaining(infosUser));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfosUser>>();
      const infosUser = { id: 123 };
      jest.spyOn(infosUserFormService, 'getInfosUser').mockReturnValue({ id: null });
      jest.spyOn(infosUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosUser: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: infosUser }));
      saveSubject.complete();

      // THEN
      expect(infosUserFormService.getInfosUser).toHaveBeenCalled();
      expect(infosUserService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IInfosUser>>();
      const infosUser = { id: 123 };
      jest.spyOn(infosUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ infosUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(infosUserService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 'ABC' };
        const entity2 = { id: 'CBA' };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
