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

describe('RessourceAide e2e test', () => {
  const ressourceAidePageUrl = '/microserviceaide/ressource-aide';
  const ressourceAidePageUrlPattern = new RegExp('/microserviceaide/ressource-aide(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ressourceAideSample = { nom: 'jusqu’à ce que' };

  let ressourceAide;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceaide/api/ressource-aides+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceaide/api/ressource-aides').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceaide/api/ressource-aides/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ressourceAide) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceaide/api/ressource-aides/${ressourceAide.id}`,
      }).then(() => {
        ressourceAide = undefined;
      });
    }
  });

  it('RessourceAides menu should load RessourceAides page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceaide/ressource-aide');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RessourceAide').should('exist');
    cy.url().should('match', ressourceAidePageUrlPattern);
  });

  describe('RessourceAide page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ressourceAidePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RessourceAide page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceaide/ressource-aide/new$'));
        cy.getEntityCreateUpdateHeading('RessourceAide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ressourceAidePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceaide/api/ressource-aides',
          body: ressourceAideSample,
        }).then(({ body }) => {
          ressourceAide = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceaide/api/ressource-aides+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceaide/api/ressource-aides?page=0&size=20>; rel="last",<http://localhost/services/microserviceaide/api/ressource-aides?page=0&size=20>; rel="first"',
              },
              body: [ressourceAide],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ressourceAidePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RessourceAide page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ressourceAide');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ressourceAidePageUrlPattern);
      });

      it('edit button click should load edit RessourceAide page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RessourceAide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ressourceAidePageUrlPattern);
      });

      it('edit button click should load edit RessourceAide page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RessourceAide');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ressourceAidePageUrlPattern);
      });

      it('last delete button click should delete instance of RessourceAide', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ressourceAide').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ressourceAidePageUrlPattern);

        ressourceAide = undefined;
      });
    });
  });

  describe('new RessourceAide page', () => {
    beforeEach(() => {
      cy.visit(`${ressourceAidePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RessourceAide');
    });

    it('should create an instance of RessourceAide', () => {
      cy.get(`[data-cy="nom"]`).type('goûter');
      cy.get(`[data-cy="nom"]`).should('have.value', 'goûter');

      cy.get(`[data-cy="libelle"]`).type('autant');
      cy.get(`[data-cy="libelle"]`).should('have.value', 'autant');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ressourceAide = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ressourceAidePageUrlPattern);
    });
  });
});
