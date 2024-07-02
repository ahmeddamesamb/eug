import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TypeselectionListComponent } from './typeselection-list.component';

describe('TypeselectionListComponent', () => {
  let component: TypeselectionListComponent;
  let fixture: ComponentFixture<TypeselectionListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TypeselectionListComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TypeselectionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
