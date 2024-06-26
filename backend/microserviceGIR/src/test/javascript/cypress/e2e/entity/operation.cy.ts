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

describe('Operation e2e test', () => {
  const operationPageUrl = '/microservicegir/operation';
  const operationPageUrlPattern = new RegExp('/microservicegir/operation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const operationSample = {};

  let operation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/operations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/operations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/operations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (operation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/operations/${operation.id}`,
      }).then(() => {
        operation = undefined;
      });
    }
  });

  it('Operations menu should load Operations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/operation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Operation').should('exist');
    cy.url().should('match', operationPageUrlPattern);
  });

  describe('Operation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(operationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Operation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/operation/new$'));
        cy.getEntityCreateUpdateHeading('Operation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/operations',
          body: operationSample,
        }).then(({ body }) => {
          operation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/operations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/operations?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/operations?page=0&size=20>; rel="first"',
              },
              body: [operation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(operationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Operation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('operation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });

      it('edit button click should load edit Operation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });

      it('edit button click should load edit Operation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });

      it('last delete button click should delete instance of Operation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('operation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);

        operation = undefined;
      });
    });
  });

  describe('new Operation page', () => {
    beforeEach(() => {
      cy.visit(`${operationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Operation');
    });

    it('should create an instance of Operation', () => {
      cy.get(`[data-cy="dateExecution"]`).type('2024-06-25T20:20');
      cy.get(`[data-cy="dateExecution"]`).blur();
      cy.get(`[data-cy="dateExecution"]`).should('have.value', '2024-06-25T20:20');

      cy.get(`[data-cy="emailUser"]`).type('attacher');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'attacher');

      cy.get(`[data-cy="detailOperation"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="detailOperation"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="infoSysteme"]`).type('jeune enfant placide');
      cy.get(`[data-cy="infoSysteme"]`).should('have.value', 'jeune enfant placide');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        operation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', operationPageUrlPattern);
    });
  });
});
