import { Component } from '@angular/core';
import { EtudiantModel } from 'src/app/views/gir/gestion-etudiants/models/etudiant-model';
import { EtudiantService } from 'src/app/views/gir/gestion-etudiants/services/etudiant.service';
import { InformationPersonellesModel } from 'src/app/views/gir/gestion-etudiants/models/information-personelles-model';
import { InformationPersonnellesService } from 'src/app/views/gir/gestion-etudiants/services/information-personnelles.service';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { map, Observable } from 'rxjs';
@Component({
  selector: 'app-profil-etudiant',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './profil-etudiant.component.html',
  styleUrl: './profil-etudiant.component.scss'
})
export class ProfilEtudiantComponent {
  informationPersonelle:InformationPersonellesModel={};
  etudiant:EtudiantModel ={};

  constructor(private activeRoute: ActivatedRoute,private router: Router,
               private infoPersonnelleService: InformationPersonnellesService,
               private etudiantService: EtudiantService){

  }

  id: string ="";
  loadState : boolean = false;

  ngOnInit(): void {
    //this.id = this.activeRoute.snapshot.paramMap.get('id')!;
    const id: Observable<string> = this.activeRoute.params.pipe(map(p=>p['id']));
    this.loadState  = true;
    id.subscribe((id)=>{
      console.log(id);
      this.etudiantService.getEtudiantById(parseInt(id)).subscribe((data)=>{
        this.etudiant = data;
        console.log(this.etudiant );
        if(this.etudiant && this.etudiant.codeEtu){
          this.infoPersonnelleService.getEtudiantCode(this.etudiant.codeEtu).subscribe((data)=>{
            this.informationPersonelle = data;
            console.log(this.informationPersonelle );
            this.loadState = false
          })

        }
        
        this.loadState = false
      })
    })
  }


}
