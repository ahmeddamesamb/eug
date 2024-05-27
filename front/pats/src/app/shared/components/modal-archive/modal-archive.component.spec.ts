import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModalArchiveComponent } from './modal-archive.component';

describe('ModalArchiveComponent', () => {
  let component: ModalArchiveComponent;
  let fixture: ComponentFixture<ModalArchiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalArchiveComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModalArchiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
