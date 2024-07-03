import { Component } from '@angular/core';
import { EtudiantModel } from 'src/app/views/gir/gestion-etudiants/models/etudiant-model';
import { InformationPersonellesModel } from 'src/app/views/gir/gestion-etudiants/models/information-personelles-model';
import { DatePipe } from '@angular/common';
@Component({
  selector: 'app-profil-etudiant',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './profil-etudiant.component.html',
  styleUrl: './profil-etudiant.component.scss'
})
export class ProfilEtudiantComponent {
  informationPersonelle:InformationPersonellesModel= {
    prenomEtu: 'PTeste',
    nomEtu: 'NTeste',
    etudiant : {
      codeEtu: 'P35295',
      dateNaissEtu: new Date ('8/11/1996'),
      ine: 'N00C8A522544'
    },
    
  }


}
