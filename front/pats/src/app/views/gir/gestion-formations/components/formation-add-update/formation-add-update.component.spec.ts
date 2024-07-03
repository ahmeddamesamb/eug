import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormationAddUpdateComponent } from './formation-add-update.component';

describe('FormationAddUpdateComponent', () => {
  let component: FormationAddUpdateComponent;
  let fixture: ComponentFixture<FormationAddUpdateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormationAddUpdateComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormationAddUpdateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
