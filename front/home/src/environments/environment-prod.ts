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

  export const environment = {
    production: true,
    urlLogin: 'http://196.1.99.13:8080/realms/',
    realm_pats: 'pats',
    realm_per: 'per',
    realm_etudiant: 'etudiant',
    redirectUrl_pats: 'http://196.1.99.31/fpats/',
    redirectUrl_per: 'http://196.1.99.31/fper',
    redirectUrl_etudiant: 'http://196.1.99.31/fetudiant',
    clientId: 'e-ugb',
    suiteUrlLogin: '/protocol/openid-connect/auth?client_id='
  };
 