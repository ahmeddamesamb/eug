export const ENVIRONMENT = {
  production: false,
  UrlEspaceHome : 'http://localhost:4200',
  UrlEspacePats : 'http://localhost:4201',
  UrlEspacePer : 'http://localhost:4202',
  UrlEspaceEtudiant : 'http://localhost:4203',
  endpointURL: 'http://localhost:8081/services/'

}
export const KeycloakConfig = {
production: false,
  keycloak: {
    url: 'http://localhost:9080/',
    realm: 'pats',
    clientId: 'e-ugb',
    'ssl-required': "external",
    "public-client": true,
  },
};

export const environment = {
  production: false,
  urlLogin: 'http://localhost:9080/realms/',
  realm_pats: 'pats',
  realm_per: 'per',
  realm_etudiant: 'etudiant',
  redirectUrl_pats: 'http://localhost:4201/',
  redirectUrl_per: 'http://localhost:4202/',
  redirectUrl_etudiant: 'http://localhost:4203/',
  clientId: 'e-ugb',
  suiteUrlLogin: '/protocol/openid-connect/auth?client_id='
};
