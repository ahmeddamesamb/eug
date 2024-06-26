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

describe('TypeOperation e2e test', () => {
  const typeOperationPageUrl = '/microservicegir/type-operation';
  const typeOperationPageUrlPattern = new RegExp('/microservicegir/type-operation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeOperationSample = { libelleTypeOperation: 'préparer fonctionnaire' };

  let typeOperation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-operations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-operations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-operations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeOperation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-operations/${typeOperation.id}`,
      }).then(() => {
        typeOperation = undefined;
      });
    }
  });

  it('TypeOperations menu should load TypeOperations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-operation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeOperation').should('exist');
    cy.url().should('match', typeOperationPageUrlPattern);
  });

  describe('TypeOperation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeOperationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeOperation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-operation/new$'));
        cy.getEntityCreateUpdateHeading('TypeOperation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOperationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-operations',
          body: typeOperationSample,
        }).then(({ body }) => {
          typeOperation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-operations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-operations?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-operations?page=0&size=20>; rel="first"',
              },
              body: [typeOperation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeOperationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeOperation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeOperation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOperationPageUrlPattern);
      });

      it('edit button click should load edit TypeOperation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOperation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOperationPageUrlPattern);
      });

      it('edit button click should load edit TypeOperation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeOperation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOperationPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeOperation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeOperation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeOperationPageUrlPattern);

        typeOperation = undefined;
      });
    });
  });

  describe('new TypeOperation page', () => {
    beforeEach(() => {
      cy.visit(`${typeOperationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeOperation');
    });

    it('should create an instance of TypeOperation', () => {
      cy.get(`[data-cy="libelleTypeOperation"]`).type('à force de');
      cy.get(`[data-cy="libelleTypeOperation"]`).should('have.value', 'à force de');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeOperation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeOperationPageUrlPattern);
    });
  });
});
