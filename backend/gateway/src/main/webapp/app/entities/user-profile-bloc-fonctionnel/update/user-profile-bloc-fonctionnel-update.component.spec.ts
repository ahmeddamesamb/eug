import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IUserProfile } from 'app/entities/user-profile/user-profile.model';
import { UserProfileService } from 'app/entities/user-profile/service/user-profile.service';
import { IBlocFonctionnel } from 'app/entities/bloc-fonctionnel/bloc-fonctionnel.model';
import { BlocFonctionnelService } from 'app/entities/bloc-fonctionnel/service/bloc-fonctionnel.service';
import { IUserProfileBlocFonctionnel } from '../user-profile-bloc-fonctionnel.model';
import { UserProfileBlocFonctionnelService } from '../service/user-profile-bloc-fonctionnel.service';
import { UserProfileBlocFonctionnelFormService } from './user-profile-bloc-fonctionnel-form.service';

import { UserProfileBlocFonctionnelUpdateComponent } from './user-profile-bloc-fonctionnel-update.component';

describe('UserProfileBlocFonctionnel Management Update Component', () => {
  let comp: UserProfileBlocFonctionnelUpdateComponent;
  let fixture: ComponentFixture<UserProfileBlocFonctionnelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let userProfileBlocFonctionnelFormService: UserProfileBlocFonctionnelFormService;
  let userProfileBlocFonctionnelService: UserProfileBlocFonctionnelService;
  let userProfileService: UserProfileService;
  let blocFonctionnelService: BlocFonctionnelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), UserProfileBlocFonctionnelUpdateComponent],
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
      .overrideTemplate(UserProfileBlocFonctionnelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(UserProfileBlocFonctionnelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    userProfileBlocFonctionnelFormService = TestBed.inject(UserProfileBlocFonctionnelFormService);
    userProfileBlocFonctionnelService = TestBed.inject(UserProfileBlocFonctionnelService);
    userProfileService = TestBed.inject(UserProfileService);
    blocFonctionnelService = TestBed.inject(BlocFonctionnelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call UserProfile query and add missing value', () => {
      const userProfileBlocFonctionnel: IUserProfileBlocFonctionnel = { id: 456 };
      const userProfil: IUserProfile = { id: 2499 };
      userProfileBlocFonctionnel.userProfil = userProfil;

      const userProfileCollection: IUserProfile[] = [{ id: 31672 }];
      jest.spyOn(userProfileService, 'query').mockReturnValue(of(new HttpResponse({ body: userProfileCollection })));
      const additionalUserProfiles = [userProfil];
      const expectedCollection: IUserProfile[] = [...additionalUserProfiles, ...userProfileCollection];
      jest.spyOn(userProfileService, 'addUserProfileToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProfileBlocFonctionnel });
      comp.ngOnInit();

      expect(userProfileService.query).toHaveBeenCalled();
      expect(userProfileService.addUserProfileToCollectionIfMissing).toHaveBeenCalledWith(
        userProfileCollection,
        ...additionalUserProfiles.map(expect.objectContaining),
      );
      expect(comp.userProfilesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call BlocFonctionnel query and add missing value', () => {
      const userProfileBlocFonctionnel: IUserProfileBlocFonctionnel = { id: 456 };
      const blocFonctionnel: IBlocFonctionnel = { id: 2962 };
      userProfileBlocFonctionnel.blocFonctionnel = blocFonctionnel;

      const blocFonctionnelCollection: IBlocFonctionnel[] = [{ id: 10443 }];
      jest.spyOn(blocFonctionnelService, 'query').mockReturnValue(of(new HttpResponse({ body: blocFonctionnelCollection })));
      const additionalBlocFonctionnels = [blocFonctionnel];
      const expectedCollection: IBlocFonctionnel[] = [...additionalBlocFonctionnels, ...blocFonctionnelCollection];
      jest.spyOn(blocFonctionnelService, 'addBlocFonctionnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ userProfileBlocFonctionnel });
      comp.ngOnInit();

      expect(blocFonctionnelService.query).toHaveBeenCalled();
      expect(blocFonctionnelService.addBlocFonctionnelToCollectionIfMissing).toHaveBeenCalledWith(
        blocFonctionnelCollection,
        ...additionalBlocFonctionnels.map(expect.objectContaining),
      );
      expect(comp.blocFonctionnelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const userProfileBlocFonctionnel: IUserProfileBlocFonctionnel = { id: 456 };
      const userProfil: IUserProfile = { id: 5527 };
      userProfileBlocFonctionnel.userProfil = userProfil;
      const blocFonctionnel: IBlocFonctionnel = { id: 11685 };
      userProfileBlocFonctionnel.blocFonctionnel = blocFonctionnel;

      activatedRoute.data = of({ userProfileBlocFonctionnel });
      comp.ngOnInit();

      expect(comp.userProfilesSharedCollection).toContain(userProfil);
      expect(comp.blocFonctionnelsSharedCollection).toContain(blocFonctionnel);
      expect(comp.userProfileBlocFonctionnel).toEqual(userProfileBlocFonctionnel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfileBlocFonctionnel>>();
      const userProfileBlocFonctionnel = { id: 123 };
      jest.spyOn(userProfileBlocFonctionnelFormService, 'getUserProfileBlocFonctionnel').mockReturnValue(userProfileBlocFonctionnel);
      jest.spyOn(userProfileBlocFonctionnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfileBlocFonctionnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProfileBlocFonctionnel }));
      saveSubject.complete();

      // THEN
      expect(userProfileBlocFonctionnelFormService.getUserProfileBlocFonctionnel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(userProfileBlocFonctionnelService.update).toHaveBeenCalledWith(expect.objectContaining(userProfileBlocFonctionnel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfileBlocFonctionnel>>();
      const userProfileBlocFonctionnel = { id: 123 };
      jest.spyOn(userProfileBlocFonctionnelFormService, 'getUserProfileBlocFonctionnel').mockReturnValue({ id: null });
      jest.spyOn(userProfileBlocFonctionnelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfileBlocFonctionnel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: userProfileBlocFonctionnel }));
      saveSubject.complete();

      // THEN
      expect(userProfileBlocFonctionnelFormService.getUserProfileBlocFonctionnel).toHaveBeenCalled();
      expect(userProfileBlocFonctionnelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IUserProfileBlocFonctionnel>>();
      const userProfileBlocFonctionnel = { id: 123 };
      jest.spyOn(userProfileBlocFonctionnelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ userProfileBlocFonctionnel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(userProfileBlocFonctionnelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareUserProfile', () => {
      it('Should forward to userProfileService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userProfileService, 'compareUserProfile');
        comp.compareUserProfile(entity, entity2);
        expect(userProfileService.compareUserProfile).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBlocFonctionnel', () => {
      it('Should forward to blocFonctionnelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(blocFonctionnelService, 'compareBlocFonctionnel');
        comp.compareBlocFonctionnel(entity, entity2);
        expect(blocFonctionnelService.compareBlocFonctionnel).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
