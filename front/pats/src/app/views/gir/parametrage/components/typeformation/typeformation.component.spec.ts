import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeformationComponent } from './typeformation.component';

describe('TypeformationComponent', () => {
  let component: TypeformationComponent;
  let fixture: ComponentFixture<TypeformationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeformationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
