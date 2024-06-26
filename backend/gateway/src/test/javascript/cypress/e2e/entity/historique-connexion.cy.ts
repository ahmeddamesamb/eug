import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('HistoriqueConnexion e2e test', () => {
  const historiqueConnexionPageUrl = '/historique-connexion';
  const historiqueConnexionPageUrlPattern = new RegExp('/historique-connexion(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const historiqueConnexionSample = { dateDebutConnexion: '2024-06-25', dateFinConnexion: '2024-06-25', actifYN: false };

  let historiqueConnexion;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/historique-connexions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/historique-connexions').as('postEntityRequest');
    cy.intercept('DELETE', '/api/historique-connexions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (historiqueConnexion) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/historique-connexions/${historiqueConnexion.id}`,
      }).then(() => {
        historiqueConnexion = undefined;
      });
    }
  });

  it('HistoriqueConnexions menu should load HistoriqueConnexions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('historique-connexion');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('HistoriqueConnexion').should('exist');
    cy.url().should('match', historiqueConnexionPageUrlPattern);
  });

  describe('HistoriqueConnexion page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(historiqueConnexionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create HistoriqueConnexion page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/historique-connexion/new$'));
        cy.getEntityCreateUpdateHeading('HistoriqueConnexion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueConnexionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/historique-connexions',
          body: historiqueConnexionSample,
        }).then(({ body }) => {
          historiqueConnexion = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/historique-connexions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/historique-connexions?page=0&size=20>; rel="last",<http://localhost/api/historique-connexions?page=0&size=20>; rel="first"',
              },
              body: [historiqueConnexion],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(historiqueConnexionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details HistoriqueConnexion page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('historiqueConnexion');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueConnexionPageUrlPattern);
      });

      it('edit button click should load edit HistoriqueConnexion page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriqueConnexion');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueConnexionPageUrlPattern);
      });

      it('edit button click should load edit HistoriqueConnexion page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('HistoriqueConnexion');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueConnexionPageUrlPattern);
      });

      it('last delete button click should delete instance of HistoriqueConnexion', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('historiqueConnexion').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', historiqueConnexionPageUrlPattern);

        historiqueConnexion = undefined;
      });
    });
  });

  describe('new HistoriqueConnexion page', () => {
    beforeEach(() => {
      cy.visit(`${historiqueConnexionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('HistoriqueConnexion');
    });

    it('should create an instance of HistoriqueConnexion', () => {
      cy.get(`[data-cy="dateDebutConnexion"]`).type('2024-06-25');
      cy.get(`[data-cy="dateDebutConnexion"]`).blur();
      cy.get(`[data-cy="dateDebutConnexion"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="dateFinConnexion"]`).type('2024-06-25');
      cy.get(`[data-cy="dateFinConnexion"]`).blur();
      cy.get(`[data-cy="dateFinConnexion"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="adresseIp"]`).type('hors');
      cy.get(`[data-cy="adresseIp"]`).should('have.value', 'hors');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        historiqueConnexion = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', historiqueConnexionPageUrlPattern);
    });
  });
});
