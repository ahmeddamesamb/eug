import { DOCUMENT } from '@angular/common';
import { inject, Injectable, Renderer2, RendererFactory2 } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScriptInjectorService {
  document: Document = inject(DOCUMENT);
  renderer: Renderer2;
  rendererFactory = inject(RendererFactory2);

  constructor() {
    this.renderer = this.rendererFactory.createRenderer(null, null);
  }

  src = 'https://unpkg.com/@googlemaps/markerclustererplus/dist/index.min.js';
  loaded: boolean = false;

  loadScript() {
    return new Promise((resolve, reject) => {
      const scriptElement = this.renderer.createElement('script');
      this.renderer.setAttribute(scriptElement, 'type', 'text/javascript');
      this.renderer.setAttribute(scriptElement, 'src', this.src);
      scriptElement.onload = () => {
        this.loaded = true;
        resolve({ loaded: true });
      };
      scriptElement.onerror = (error: any) => reject({ loaded: false, error });
      this.renderer.appendChild(this.document.head, scriptElement);
    });
  }
}
