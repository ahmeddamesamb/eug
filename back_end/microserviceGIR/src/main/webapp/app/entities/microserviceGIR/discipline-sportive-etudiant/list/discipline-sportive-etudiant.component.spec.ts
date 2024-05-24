import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisciplineSportiveEtudiantService } from '../service/discipline-sportive-etudiant.service';

import { DisciplineSportiveEtudiantComponent } from './discipline-sportive-etudiant.component';
import SpyInstance = jest.SpyInstance;

describe('DisciplineSportiveEtudiant Management Component', () => {
  let comp: DisciplineSportiveEtudiantComponent;
  let fixture: ComponentFixture<DisciplineSportiveEtudiantComponent>;
  let service: DisciplineSportiveEtudiantService;
  let routerNavigateSpy: SpyInstance<Promise<boolean>>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([
          { path: 'microservicegir/discipline-sportive-etudiant', component: DisciplineSportiveEtudiantComponent },
        ]),
        HttpClientTestingModule,
        DisciplineSportiveEtudiantComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(DisciplineSportiveEtudiantComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisciplineSportiveEtudiantComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DisciplineSportiveEtudiantService);
    routerNavigateSpy = jest.spyOn(comp.router, 'navigate');

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.disciplineSportiveEtudiants?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to disciplineSportiveEtudiantService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getDisciplineSportiveEtudiantIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getDisciplineSportiveEtudiantIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });

  it('should load a page', () => {
    // WHEN
    comp.navigateToPage(1);

    // THEN
    expect(routerNavigateSpy).toHaveBeenCalled();
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['id,desc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.navigateToWithComponentValues();

    // THEN
    expect(routerNavigateSpy).toHaveBeenLastCalledWith(
      expect.anything(),
      expect.objectContaining({
        queryParams: expect.objectContaining({
          sort: ['name,asc'],
        }),
      }),
    );
  });
});
