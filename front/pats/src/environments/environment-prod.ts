export const ENVIRONMENT = {
    production: true,
    UrlEspaceHome : 'http://196.1.99.31/fhome',
    UrlEspacePats : 'http://196.1.99.31/fpats',
    UrlEspacePer : 'http://196.1.99.31/fper',
    UrlEspaceEtudiant : 'http://196.1.99.31/fetudiant',
    endpointURL: 'http://196.1.99.31:7000/services/'

}
export const KeycloakConfig = {
    production: true,
    keycloak: {
      url: 'http://196.1.99.13:8080/',
      realm: 'pats',
      clientId: 'e-ugb',
      'ssl-required': "external",
      "public-client": true,
    },
  };
 