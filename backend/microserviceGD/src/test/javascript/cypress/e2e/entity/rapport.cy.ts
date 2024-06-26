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

describe('Rapport e2e test', () => {
  const rapportPageUrl = '/microservicegd/rapport';
  const rapportPageUrlPattern = new RegExp('/microservicegd/rapport(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rapportSample = {};

  let rapport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegd/api/rapports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegd/api/rapports').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegd/api/rapports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (rapport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegd/api/rapports/${rapport.id}`,
      }).then(() => {
        rapport = undefined;
      });
    }
  });

  it('Rapports menu should load Rapports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegd/rapport');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Rapport').should('exist');
    cy.url().should('match', rapportPageUrlPattern);
  });

  describe('Rapport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rapportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Rapport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegd/rapport/new$'));
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegd/api/rapports',
          body: rapportSample,
        }).then(({ body }) => {
          rapport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegd/api/rapports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegd/api/rapports?page=0&size=20>; rel="last",<http://localhost/services/microservicegd/api/rapports?page=0&size=20>; rel="first"',
              },
              body: [rapport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(rapportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Rapport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('rapport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('edit button click should load edit Rapport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('edit button click should load edit Rapport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Rapport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);
      });

      it('last delete button click should delete instance of Rapport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('rapport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', rapportPageUrlPattern);

        rapport = undefined;
      });
    });
  });

  describe('new Rapport page', () => {
    beforeEach(() => {
      cy.visit(`${rapportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Rapport');
    });

    it('should create an instance of Rapport', () => {
      cy.get(`[data-cy="libelleRapport"]`).type('sombre');
      cy.get(`[data-cy="libelleRapport"]`).should('have.value', 'sombre');

      cy.get(`[data-cy="descriptionRapport"]`).type('réparer accumuler entre-temps');
      cy.get(`[data-cy="descriptionRapport"]`).should('have.value', 'réparer accumuler entre-temps');

      cy.get(`[data-cy="contenuRapport"]`).type('ailleurs');
      cy.get(`[data-cy="contenuRapport"]`).should('have.value', 'ailleurs');

      cy.get(`[data-cy="dateRedaction"]`).type('2024-06-25T07:34');
      cy.get(`[data-cy="dateRedaction"]`).blur();
      cy.get(`[data-cy="dateRedaction"]`).should('have.value', '2024-06-25T07:34');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        rapport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', rapportPageUrlPattern);
    });
  });
});
