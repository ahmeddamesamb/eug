import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeadmissionListComponent } from './typeadmission-list.component';

describe('TypeadmissionListComponent', () => {
  let component: TypeadmissionListComponent;
  let fixture: ComponentFixture<TypeadmissionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeadmissionListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeadmissionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
