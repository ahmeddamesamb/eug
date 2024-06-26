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

describe('Deliberation e2e test', () => {
  const deliberationPageUrl = '/microservicedeliberation/deliberation';
  const deliberationPageUrlPattern = new RegExp('/microservicedeliberation/deliberation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const deliberationSample = {};

  let deliberation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicedeliberation/api/deliberations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicedeliberation/api/deliberations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicedeliberation/api/deliberations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (deliberation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicedeliberation/api/deliberations/${deliberation.id}`,
      }).then(() => {
        deliberation = undefined;
      });
    }
  });

  it('Deliberations menu should load Deliberations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicedeliberation/deliberation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Deliberation').should('exist');
    cy.url().should('match', deliberationPageUrlPattern);
  });

  describe('Deliberation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(deliberationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Deliberation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicedeliberation/deliberation/new$'));
        cy.getEntityCreateUpdateHeading('Deliberation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliberationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicedeliberation/api/deliberations',
          body: deliberationSample,
        }).then(({ body }) => {
          deliberation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicedeliberation/api/deliberations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicedeliberation/api/deliberations?page=0&size=20>; rel="last",<http://localhost/services/microservicedeliberation/api/deliberations?page=0&size=20>; rel="first"',
              },
              body: [deliberation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(deliberationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Deliberation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('deliberation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliberationPageUrlPattern);
      });

      it('edit button click should load edit Deliberation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Deliberation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliberationPageUrlPattern);
      });

      it('edit button click should load edit Deliberation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Deliberation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliberationPageUrlPattern);
      });

      it('last delete button click should delete instance of Deliberation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('deliberation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', deliberationPageUrlPattern);

        deliberation = undefined;
      });
    });
  });

  describe('new Deliberation page', () => {
    beforeEach(() => {
      cy.visit(`${deliberationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Deliberation');
    });

    it('should create an instance of Deliberation', () => {
      cy.get(`[data-cy="estValideeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="estValideeYN"]`).click();
      cy.get(`[data-cy="estValideeYN"]`).should('be.checked');

      cy.setFieldImageAsBytesOfEntity('pvDeliberation', 'integration-test.png', 'image/png');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        deliberation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', deliberationPageUrlPattern);
    });
  });
});
