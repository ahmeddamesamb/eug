import { Component } from '@angular/core';

import { MentionListComponent } from '../../components/mention/components/mention-list/mention-list.component';

@Component({
  selector: 'app-mention',
  standalone: true,
  imports: [MentionListComponent],
  templateUrl: './mention.component.html',
  styleUrl: './mention.component.scss'
})
export class MentionComponent {

}
