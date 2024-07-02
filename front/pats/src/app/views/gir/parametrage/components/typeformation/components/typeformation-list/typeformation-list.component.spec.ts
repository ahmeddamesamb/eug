import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeformationListComponent } from './typeformation-list.component';

describe('TypeformationListComponent', () => {
  let component: TypeformationListComponent;
  let fixture: ComponentFixture<TypeformationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeformationListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeformationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
