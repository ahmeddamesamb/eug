import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';

import { CardModule, CarouselModule, GridModule } from '@coreui/angular-pro';
import { IconModule } from '@coreui/icons-angular';
import { IconSetService } from '@coreui/icons-angular';
import { iconSubset } from '../../../shared/icons/icon-subset';
import { CarouselsComponent } from './carousels.component';

describe('CarouselsComponent', () => {
  let component: CarouselsComponent;
  let fixture: ComponentFixture<CarouselsComponent>;
  let iconSetService: IconSetService

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
    imports: [CarouselModule, NoopAnimationsModule, CardModule, GridModule, IconModule, RouterTestingModule, CarouselsComponent],
    providers: [IconSetService]
})
    .compileComponents();
  }));

  beforeEach(() => {
    iconSetService = TestBed.inject(IconSetService);
    iconSetService.icons = { ...iconSubset };

    fixture = TestBed.createComponent(CarouselsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
