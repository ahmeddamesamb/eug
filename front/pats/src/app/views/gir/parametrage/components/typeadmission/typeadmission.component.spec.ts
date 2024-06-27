import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeadmissionComponent } from './typeadmission.component';

describe('TypeadmissionComponent', () => {
  let component: TypeadmissionComponent;
  let fixture: ComponentFixture<TypeadmissionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeadmissionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeadmissionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
