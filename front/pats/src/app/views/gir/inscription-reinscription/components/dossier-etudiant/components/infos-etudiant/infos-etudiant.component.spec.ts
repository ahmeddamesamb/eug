import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InfosEtudiantComponent } from './infos-etudiant.component';

describe('InfosEtudiantComponent', () => {
  let component: InfosEtudiantComponent;
  let fixture: ComponentFixture<InfosEtudiantComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InfosEtudiantComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InfosEtudiantComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
