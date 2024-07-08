import { NgStyle, NgTemplateOutlet } from '@angular/common';
import { Component, inject, Input } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import {
  AvatarComponent,
  BadgeComponent,
  BreadcrumbRouterComponent,
  ButtonDirective,
  ColorModeService,
  ContainerComponent,
  DropdownComponent,
  DropdownDividerDirective,
  DropdownHeaderDirective,
  DropdownItemDirective,
  DropdownMenuDirective,
  DropdownToggleDirective,
  FormCheckComponent,
  FormCheckInputDirective,
  FormCheckLabelDirective,
  FormControlDirective,
  FormDirective,
  FormFeedbackComponent,
  FormLabelDirective,
  FormSelectDirective,
  HeaderComponent,
  HeaderNavComponent,
  HeaderTogglerDirective,
  InputGroupComponent,
  InputGroupTextDirective,
  ListGroupDirective,
  ListGroupItemDirective,
  MultiSelectOptgroupComponent,
  MultiSelectOptionComponent,
  NavItemComponent,
  NavLinkDirective,
  ProgressBarDirective,
  ProgressComponent,
  SidebarToggleDirective,
  TextColorDirective,
  ThemeDirective
} from '@coreui/angular-pro';

import { IconDirective } from '@coreui/icons-angular';
import { DefaultBreadcrumbComponent } from '.././';
import { UserService } from '../../../services/user.service';
import { KeycloakService } from 'keycloak-angular';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { InformationPersonellesModel } from 'src/app/views/gir/gestion-etudiants/models/information-personelles-model';
import { InformationPersonnellesService } from 'src/app/views/gir/gestion-etudiants/services/information-personnelles.service';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  styleUrls: ['./default-header.component.scss'],
  standalone: true,
  providers: [UserService, KeycloakService],
  imports: [ContainerComponent, HeaderTogglerDirective, SidebarToggleDirective,
    IconDirective, HeaderNavComponent, NavItemComponent, NavLinkDirective,
    RouterLink, RouterLinkActive, NgTemplateOutlet, BreadcrumbRouterComponent,
    ThemeDirective, DropdownComponent, DropdownToggleDirective, TextColorDirective,
    AvatarComponent, DropdownMenuDirective, DropdownHeaderDirective,
    DropdownItemDirective, BadgeComponent, DropdownDividerDirective,
    ProgressBarDirective, ProgressComponent, InputGroupComponent,
    InputGroupTextDirective, FormControlDirective, ButtonDirective,
    NgStyle, FormDirective,DefaultBreadcrumbComponent, ReactiveFormsModule,
    FormsModule, FormDirective, FormLabelDirective,
    FormControlDirective, FormFeedbackComponent,
    InputGroupComponent, InputGroupTextDirective,
    FormSelectDirective, FormCheckComponent, FormCheckInputDirective, FormCheckLabelDirective, ButtonDirective, ListGroupDirective, ListGroupItemDirective, MultiSelectOptionComponent, MultiSelectOptgroupComponent]
})
export class DefaultHeaderComponent extends HeaderComponent {

  readonly #colorModeService = inject(ColorModeService);
  readonly colorMode = this.#colorModeService.colorMode;
  isLoggedIn: boolean = false;
  
  userFirstName: string | undefined;
  userLastName: string | undefined;
  rechercheForm: FormGroup;
  informationPersonelle:InformationPersonellesModel={};


  constructor(private userService: UserService  , private infoPersonnelleService: InformationPersonnellesService, private router:Router ) {
    super();
    this.#colorModeService.localStorageItemName.set('coreui-pro-angular-admin-template-theme-modern');
    this.#colorModeService.eventName.set('ColorSchemeChange');

    this.rechercheForm = new FormGroup({
      code: new FormControl('', Validators.required),

    });
  }

  @Input() sidebarId: string = 'sidebar1';



  

  ngOnInit() {
    const token = this.userService.getToken();
    console.log("Token récupéré depuis le localStorage :", token);
    this.userService.initializeWithToken(token ?? '');
    this.userService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isLoggedIn = isLoggedIn;
      if (isLoggedIn) {
        // Mettre à jour les variables userFirstName et userLastName
        this.userFirstName = this.userService.getUserFirstName();
        this.userLastName = this.userService.getUserLastName();
      }
    });

  
  }

  login() {
    this.userService.login();
  }

  logout() {
    this.userService.logout();
  }

  dossierEtudiant(){
    console.log("Code de recherche",this.rechercheForm.value.code);
    this.infoPersonnelleService.getEtudiantCode(this.rechercheForm.value.code).subscribe((data)=>{
      this.informationPersonelle = data;
      console.log(this.informationPersonelle );
      this.router.navigate(['/gir/inscription-reinscription/view/',this.informationPersonelle.etudiant?.id]);
    })
    
  }


}
