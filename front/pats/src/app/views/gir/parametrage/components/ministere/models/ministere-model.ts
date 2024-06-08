export interface MinistereModel {
  id:number,
  nomMinistere:string,
  sigleMinistere:string,
  dateDebut: Date | string,
  dateFin?: Date | string,
  enCoursYN?: number
}
