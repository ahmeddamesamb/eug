/// <reference types="@types/google.maps" />

import { Component, Inject, OnDestroy, OnInit, signal, ViewChild } from '@angular/core';
import { AsyncPipe, DOCUMENT } from '@angular/common';
import { GoogleMapsModule, MapInfoWindow, MapMarker } from '@angular/google-maps';
import { DocsLinkComponent } from '@docs-components/docs-link/docs-link.component';

import { GoogleMapsLoaderService } from './google-maps-loader.service';
import { ScriptInjectorService } from './script-injector.service';
import { CardBodyComponent, CardComponent, CardHeaderComponent } from '@coreui/angular-pro';

// Marker interface for type safety
interface Marker {
  position: google.maps.LatLngLiteral;
  label?: string;
  title: string;
  www: string;
}

@Component({
  selector: 'app-google-maps-integration',
  templateUrl: 'google-maps.component.html',
  styleUrls: ['google-maps.component.scss'],
  providers: [GoogleMapsLoaderService],
  standalone: true,
  imports: [DocsLinkComponent, CardComponent, CardHeaderComponent, CardBodyComponent, GoogleMapsModule, AsyncPipe]
})
export class GoogleMapsComponent implements OnInit, OnDestroy {
  // apiLoaded!: Observable<boolean>;

  title: string = '';

  options: google.maps.MapOptions = {
    center: {
      lat: 37.42000,
      lng: -122.103719
    },
    zoom: 10
  };

  // Let's cache for markerOptions to avoid multiple creation of markerOptions config objects.
  // Creates the markerOptions object at first time the getter is called, then cached.
  // Subsequent accesses return the cached value without recreating it.
  markerOptionsCache!: google.maps.MarkerOptions;

  // markerOptions getter defines a property, but does not calculate the property's value until it is accessed.
  // and it will be accessed from ng-template only when apiLoaded === true
  get markerOptions(): google.maps.MarkerOptions {
    if (!this.markerOptionsCache) {
      this.markerOptionsCache = {
        draggable: false
        // icon: {
        //   url: 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
        //   size: new google.maps.Size(40, 82),
        //   origin: new google.maps.Point(0, 0),
        //   labelOrigin: new google.maps.Point(10, -10),
        //   // The anchor for this image is the base of the flagpole at (0, 32).
        //   anchor: new google.maps.Point(0, 32),
        // },
      };
    }
    return this.markerOptionsCache;
  }

  markerPositions: google.maps.LatLngLiteral[] = [];
  markerClustererImagePath = 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m';

  markers: Marker[] = [
    {
      position: {
        lat: 37.431489,
        lng: -122.163719
      },
      label: 'S',
      title: 'Stanford',
      www: 'https://www.stanford.edu/'
    },
    {
      position: {
        lat: 37.394694,
        lng: -122.150333
      },
      label: 'T',
      title: 'Tesla',
      www: 'https://www.tesla.com/'
    },
    {
      position: {
        lat: 37.331681,
        lng: -122.0301
      },
      label: 'A',
      title: 'Apple',
      www: 'https://www.apple.com/'
    },
    {
      position: {
        lat: 37.484722,
        lng: -122.148333
      },
      label: 'F',
      title: 'Facebook',
      www: 'https://www.facebook.com/'
    }
  ];

  @ViewChild(MapInfoWindow) infoWindow!: MapInfoWindow;
  activeInfoWindow!: Marker;

  scriptLoaded = signal(false);

  constructor(
    @Inject(DOCUMENT) private document: Document,
    private scriptInjectorService: ScriptInjectorService,
    public googleMapsLoaderService: GoogleMapsLoaderService
  ) {
    this.loadMarkerClusterer();
  }

  loadMarkerClusterer() {
    if (this.scriptInjectorService.loaded) {
      this.scriptLoaded.set(true);
      return;
    }

    this.scriptInjectorService
      .loadScript()
      .catch((rejected) => {
        console.log('rejected:', rejected);
      })
      .then((resolved) => {
        console.log('resolved:', resolved);
        this.scriptLoaded.set(true);
      });
  }

  get infoWindowContent() {
    return this.activeInfoWindow;
  }

  set infoWindowContent(marker) {
    this.title = marker.title;
    this.activeInfoWindow = marker;
  }

  ngOnInit() {
    this.setPositions();
    this.setResizeObserver();
  }

  ngOnDestroy() {
    this.removeGoogleMapScript();
    this.unsetResizeObserver();
  }

  setPositions() {
    this.markers.forEach((marker) => {
      const { lat, lng } = { ...marker.position };
      this.markerPositions.push({ lat, lng });
    });
  }

  openInfoWindow(marker: MapMarker, item: Marker) {
    this.infoWindowContent = item;
    this.infoWindow.open(marker);
  }

  removeGoogleMapScript() {
    // todo: temp workaround for 'You have included the Google Maps API multiple times on this page'
    const keywords = ['https://maps.googleapis'];

    // Remove google from BOM (window object)
    // @ts-ignore
    window.google = undefined;

    // Remove google map scripts from DOM
    const scripts = this.document.head.getElementsByTagName('script');
    for (let i = scripts.length - 1; i >= 0; i--) {
      const scriptSource = scripts[i].getAttribute('src');
      if (scriptSource != null) {
        if (keywords.filter(item => scriptSource.includes(item)).length) {
          scripts[i].remove();
          // scripts[i].parentNode.removeChild(scripts[i]);
        }
      }
    }
  }

  mapHeight = signal<string>('65vh');

  #resizeObserver: ResizeObserver = new ResizeObserver(entries => {
    const height = entries[0].contentRect.height;
    if (height < 530) {
      return;
    }
    const heightString = `${height - 350}px`;
    this.mapHeight.set(heightString);
    // console.log(height, this.mapHeight());
  });

  setResizeObserver() {
    this.#resizeObserver.observe(this.document.body);
  }

  unsetResizeObserver() {
    this.#resizeObserver.unobserve(this.document.body);
  }
}
