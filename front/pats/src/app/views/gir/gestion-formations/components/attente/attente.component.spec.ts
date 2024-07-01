import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttenteComponent } from './attente.component';

describe('AttenteComponent', () => {
  let component: AttenteComponent;
  let fixture: ComponentFixture<AttenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AttenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
