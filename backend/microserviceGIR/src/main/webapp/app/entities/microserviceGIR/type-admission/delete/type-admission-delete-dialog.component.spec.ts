jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { TypeAdmissionService } from '../service/type-admission.service';

import { TypeAdmissionDeleteDialogComponent } from './type-admission-delete-dialog.component';

describe('TypeAdmission Management Delete Component', () => {
  let comp: TypeAdmissionDeleteDialogComponent;
  let fixture: ComponentFixture<TypeAdmissionDeleteDialogComponent>;
  let service: TypeAdmissionService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, TypeAdmissionDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(TypeAdmissionDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(TypeAdmissionDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(TypeAdmissionService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      }),
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
