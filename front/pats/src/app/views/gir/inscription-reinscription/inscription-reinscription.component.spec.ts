import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InscriptionReinscriptionComponent } from './inscription-reinscription.component';

describe('InscriptionReinscriptionComponent', () => {
  let component: InscriptionReinscriptionComponent;
  let fixture: ComponentFixture<InscriptionReinscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InscriptionReinscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InscriptionReinscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
