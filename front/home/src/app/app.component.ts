import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SliderComponent } from "./layouts/slider/slider.component";

@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.scss',
    imports: [RouterOutlet, SliderComponent]
})
export class AppComponent {
  title = 'home';
}
