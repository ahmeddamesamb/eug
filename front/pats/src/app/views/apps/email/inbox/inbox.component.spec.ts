import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { BadgeModule, ButtonGroupModule, CardModule, DropdownModule } from '@coreui/angular-pro';
import { InboxMessageComponent } from '../inbox-message/inbox-message.component';
import { MailToolbarComponent } from '../mail-toolbar/mail-toolbar.component';
import { InboxComponent } from './inbox.component';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule } from '@angular/forms';

describe('InboxComponent', () => {
  let component: InboxComponent;
  let fixture: ComponentFixture<InboxComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
    imports: [CardModule, RouterTestingModule, ButtonGroupModule, DropdownModule, BadgeModule, ReactiveFormsModule, InboxComponent, InboxMessageComponent, MailToolbarComponent]
}).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InboxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
