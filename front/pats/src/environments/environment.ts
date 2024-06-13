export const ENVIRONMENT = {
    production: true,
    UrlEspaceHome : 'http://localhost:4200',
    UrlEspacePats : 'http://localhost:4201',
    UrlEspacePer : 'http://localhost:4202',
    UrlEspaceEtudiant : 'http://localhost:4203',
    endpointURL: 'http://localhost:8081/services/'

}
export const KeycloakConfig = {
  production: true,
    keycloak: {
      url: 'http://localhost:9080/',
      realm: 'pats',
      clientId: 'e-ugb',
      'ssl-required': "external",
      "public-client": true,
    },
  };
 