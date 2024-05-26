import { Component, OnInit, createNgModule, Injector } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import SharedModule from 'app/shared/shared.module';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { VERSION } from 'app/app.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { LoginService } from 'app/login/login.service';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { EntityNavbarItems } from 'app/entities/entity-navbar-items';

import { loadNavbarItems, loadTranslationModule } from 'app/core/microfrontend';
import ActiveMenuDirective from './active-menu.directive';
import NavbarItem from './navbar-item.model';

@Component({
  standalone: true,
  selector: 'ugb-navbar',
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
  imports: [RouterModule, SharedModule, HasAnyAuthorityDirective, ActiveMenuDirective],
})
export default class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  languages = LANGUAGES;
  openAPIEnabled?: boolean;
  version = '';
  account: Account | null = null;
  entitiesNavbarItems: NavbarItem[] = [];
  microserviceuserEntityNavbarItems: NavbarItem[] = [];
  microservicegirEntityNavbarItems: NavbarItem[] = [];
  microserviceenseignementEntityNavbarItems: NavbarItem[] = [];
  microserviceedtEntityNavbarItems: NavbarItem[] = [];
  microservicegrhEntityNavbarItems: NavbarItem[] = [];
  microserviceauaEntityNavbarItems: NavbarItem[] = [];
  microservicedeliberationEntityNavbarItems: NavbarItem[] = [];
  microservicegdEntityNavbarItems: NavbarItem[] = [];
  microserviceaclcEntityNavbarItems: NavbarItem[] = [];
  microserviceaideEntityNavbarItems: NavbarItem[] = [];
  microservicegedEntityNavbarItems: NavbarItem[] = [];

  constructor(
    private loginService: LoginService,
    private translateService: TranslateService,
    private stateStorageService: StateStorageService,
    private injector: Injector,
    private accountService: AccountService,
    private profileService: ProfileService,
    private router: Router,
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.entitiesNavbarItems = EntityNavbarItems;
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });

    this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.loadMicrofrontendsEntities();
    });
  }

  changeLanguage(languageKey: string): void {
    this.stateStorageService.storeLocale(languageKey);
    this.translateService.use(languageKey);
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }

  login(): void {
    this.loginService.login();
  }

  logout(): void {
    this.collapseNavbar();
    this.loginService.logout();
    this.router.navigate(['']);
  }

  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }

  loadMicrofrontendsEntities(): void {
    // Lazy load microfrontend entities.
    loadNavbarItems('microserviceuser').then(
      async items => {
        this.microserviceuserEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceuser');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceuser translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceuser entities', error);
      },
    );
    loadNavbarItems('microservicegir').then(
      async items => {
        this.microservicegirEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microservicegir');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microservicegir translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microservicegir entities', error);
      },
    );
    loadNavbarItems('microserviceenseignement').then(
      async items => {
        this.microserviceenseignementEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceenseignement');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceenseignement translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceenseignement entities', error);
      },
    );
    loadNavbarItems('microserviceedt').then(
      async items => {
        this.microserviceedtEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceedt');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceedt translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceedt entities', error);
      },
    );
    loadNavbarItems('microservicegrh').then(
      async items => {
        this.microservicegrhEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microservicegrh');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microservicegrh translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microservicegrh entities', error);
      },
    );
    loadNavbarItems('microserviceaua').then(
      async items => {
        this.microserviceauaEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceaua');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceaua translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceaua entities', error);
      },
    );
    loadNavbarItems('microservicedeliberation').then(
      async items => {
        this.microservicedeliberationEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microservicedeliberation');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microservicedeliberation translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microservicedeliberation entities', error);
      },
    );
    loadNavbarItems('microservicegd').then(
      async items => {
        this.microservicegdEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microservicegd');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microservicegd translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microservicegd entities', error);
      },
    );
    loadNavbarItems('microserviceaclc').then(
      async items => {
        this.microserviceaclcEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceaclc');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceaclc translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceaclc entities', error);
      },
    );
    loadNavbarItems('microserviceaide').then(
      async items => {
        this.microserviceaideEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceaide');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceaide translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceaide entities', error);
      },
    );
    loadNavbarItems('microserviceged').then(
      async items => {
        this.microservicegedEntityNavbarItems = items;
        try {
          const LazyTranslationModule = await loadTranslationModule('microserviceged');
          createNgModule(LazyTranslationModule, this.injector);
        } catch (error) {
          // eslint-disable-next-line no-console
          console.log('Error loading microserviceged translation module', error);
        }
      },
      error => {
        // eslint-disable-next-line no-console
        console.log('Error loading microserviceged entities', error);
      },
    );
  }
}
