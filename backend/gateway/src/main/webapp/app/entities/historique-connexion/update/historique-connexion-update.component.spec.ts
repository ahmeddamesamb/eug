import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IInfosUser } from 'app/entities/infos-user/infos-user.model';
import { InfosUserService } from 'app/entities/infos-user/service/infos-user.service';
import { HistoriqueConnexionService } from '../service/historique-connexion.service';
import { IHistoriqueConnexion } from '../historique-connexion.model';
import { HistoriqueConnexionFormService } from './historique-connexion-form.service';

import { HistoriqueConnexionUpdateComponent } from './historique-connexion-update.component';

describe('HistoriqueConnexion Management Update Component', () => {
  let comp: HistoriqueConnexionUpdateComponent;
  let fixture: ComponentFixture<HistoriqueConnexionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let historiqueConnexionFormService: HistoriqueConnexionFormService;
  let historiqueConnexionService: HistoriqueConnexionService;
  let infosUserService: InfosUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), HistoriqueConnexionUpdateComponent],
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
      .overrideTemplate(HistoriqueConnexionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HistoriqueConnexionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    historiqueConnexionFormService = TestBed.inject(HistoriqueConnexionFormService);
    historiqueConnexionService = TestBed.inject(HistoriqueConnexionService);
    infosUserService = TestBed.inject(InfosUserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call InfosUser query and add missing value', () => {
      const historiqueConnexion: IHistoriqueConnexion = { id: 456 };
      const infoUser: IInfosUser = { id: 6812 };
      historiqueConnexion.infoUser = infoUser;

      const infosUserCollection: IInfosUser[] = [{ id: 15855 }];
      jest.spyOn(infosUserService, 'query').mockReturnValue(of(new HttpResponse({ body: infosUserCollection })));
      const additionalInfosUsers = [infoUser];
      const expectedCollection: IInfosUser[] = [...additionalInfosUsers, ...infosUserCollection];
      jest.spyOn(infosUserService, 'addInfosUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ historiqueConnexion });
      comp.ngOnInit();

      expect(infosUserService.query).toHaveBeenCalled();
      expect(infosUserService.addInfosUserToCollectionIfMissing).toHaveBeenCalledWith(
        infosUserCollection,
        ...additionalInfosUsers.map(expect.objectContaining),
      );
      expect(comp.infosUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const historiqueConnexion: IHistoriqueConnexion = { id: 456 };
      const infoUser: IInfosUser = { id: 16139 };
      historiqueConnexion.infoUser = infoUser;

      activatedRoute.data = of({ historiqueConnexion });
      comp.ngOnInit();

      expect(comp.infosUsersSharedCollection).toContain(infoUser);
      expect(comp.historiqueConnexion).toEqual(historiqueConnexion);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueConnexion>>();
      const historiqueConnexion = { id: 123 };
      jest.spyOn(historiqueConnexionFormService, 'getHistoriqueConnexion').mockReturnValue(historiqueConnexion);
      jest.spyOn(historiqueConnexionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueConnexion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiqueConnexion }));
      saveSubject.complete();

      // THEN
      expect(historiqueConnexionFormService.getHistoriqueConnexion).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(historiqueConnexionService.update).toHaveBeenCalledWith(expect.objectContaining(historiqueConnexion));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueConnexion>>();
      const historiqueConnexion = { id: 123 };
      jest.spyOn(historiqueConnexionFormService, 'getHistoriqueConnexion').mockReturnValue({ id: null });
      jest.spyOn(historiqueConnexionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueConnexion: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: historiqueConnexion }));
      saveSubject.complete();

      // THEN
      expect(historiqueConnexionFormService.getHistoriqueConnexion).toHaveBeenCalled();
      expect(historiqueConnexionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IHistoriqueConnexion>>();
      const historiqueConnexion = { id: 123 };
      jest.spyOn(historiqueConnexionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ historiqueConnexion });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(historiqueConnexionService.update).toHaveBeenCalled();
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
  });
});
