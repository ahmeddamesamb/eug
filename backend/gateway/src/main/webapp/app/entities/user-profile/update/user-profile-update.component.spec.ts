import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IProfil } from 'app/entities/profil/profil.model';
import { ProfilService } from 'app/entities/profil/service/profil.service';
import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { IUserProfile } from '../user-profile.model';
import { UserProfileService } from '../service/user-profile.service';
import { UserProfileFormService } from './user-profile-form.service';

import { UserProfileUpdateComponent } from './user-profile-update.component';

describe('UserProfile Management Update Component', () => {
  let comp: UserProfileUpdateComponent;
  let fixture: ComponentFixture<UserProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userProfileFormService: UserProfileFormService;
  let userProfileService: UserProfileService;
  let profilService: ProfilService;
  let infosUserService: InfosUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserProfileUpdateComponent],
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
      .overrideTemplate(UserProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userProfileFormService = TestBed.inject(UserProfileFormService);
    userProfileService = TestBed.inject(UserProfileService);
    profilService = TestBed.inject(ProfilService);
    infosUserService = TestBed.inject(InfosUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Profil query and add missing value', () => {
      const userProfile: IUserProfile = { id: 456 };
      const profil: IProfil = { id: 13353 };
      userProfile.profil = profil;

      const profilCollection: IProfil[] = [{ id: 15347 }];
      jest.spyOn(profilService, 'query').mockReturnValue(of(new HttpResponse({ body: profilCollection })));
      const additionalProfils = [profil];
      const expectedCollection: IProfil[] = [...additionalProfils, ...profilCollection];
      jest.spyOn(profilService, 'addProfilToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProfile });
      comp.ngOnInit();

      expect(profilService.query).toHaveBeenCalled();
      expect(profilService.addProfilToCollectionIfMissing).toHaveBeenCalledWith(
        profilCollection,
        ...additionalProfils.map(expect.objectContaining),
      );
      expect(comp.profilsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call InfosUser query and add missing value', () => {
      const userProfile: IUserProfile = { id: 456 };
      const infoUser: IInfosUser = { id: 15847 };
      userProfile.infoUser = infoUser;

      const infosUserCollection: IInfosUser[] = [{ id: 5895 }];
      jest.spyOn(infosUserService, 'query').mockReturnValue(of(new HttpResponse({ body: infosUserCollection })));
      const additionalInfosUsers = [infoUser];
      const expectedCollection: IInfosUser[] = [...additionalInfosUsers, ...infosUserCollection];
      jest.spyOn(infosUserService, 'addInfosUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProfile });
      comp.ngOnInit();

      expect(infosUserService.query).toHaveBeenCalled();
      expect(infosUserService.addInfosUserToCollectionIfMissing).toHaveBeenCalledWith(
        infosUserCollection,
        ...additionalInfosUsers.map(expect.objectContaining),
      );
      expect(comp.infosUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userProfile: IUserProfile = { id: 456 };
      const profil: IProfil = { id: 10935 };
      userProfile.profil = profil;
      const infoUser: IInfosUser = { id: 27143 };
      userProfile.infoUser = infoUser;

      activatedRoute.data = of({ userProfile });
      comp.ngOnInit();

      expect(comp.profilsSharedCollection).toContain(profil);
      expect(comp.infosUsersSharedCollection).toContain(infoUser);
      expect(comp.userProfile).toEqual(userProfile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfile>>();
      const userProfile = { id: 123 };
      jest.spyOn(userProfileFormService, 'getUserProfile').mockReturnValue(userProfile);
      jest.spyOn(userProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProfile }));
      saveSubject.complete();

      // THEN
      expect(userProfileFormService.getUserProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userProfileService.update).toHaveBeenCalledWith(expect.objectContaining(userProfile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfile>>();
      const userProfile = { id: 123 };
      jest.spyOn(userProfileFormService, 'getUserProfile').mockReturnValue({ id: null });
      jest.spyOn(userProfileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProfile }));
      saveSubject.complete();

      // THEN
      expect(userProfileFormService.getUserProfile).toHaveBeenCalled();
      expect(userProfileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfile>>();
      const userProfile = { id: 123 };
      jest.spyOn(userProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userProfileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareProfil', () => {
      it('Should forward to profilService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(profilService, 'compareProfil');
        comp.compareProfil(entity, entity2);
        expect(profilService.compareProfil).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareInfosUser', () => {
      it('Should forward to infosUserService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(infosUserService, 'compareInfosUser');
        comp.compareInfosUser(entity, entity2);
        expect(infosUserService.compareInfosUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
