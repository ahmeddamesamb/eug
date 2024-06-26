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

describe('TypeDocument e2e test', () => {
  const typeDocumentPageUrl = '/microserviceged/type-document';
  const typeDocumentPageUrlPattern = new RegExp('/microserviceged/type-document(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeDocumentSample = {};

  let typeDocument;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceged/api/type-documents+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceged/api/type-documents').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceged/api/type-documents/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeDocument) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceged/api/type-documents/${typeDocument.id}`,
      }).then(() => {
        typeDocument = undefined;
      });
    }
  });

  it('TypeDocuments menu should load TypeDocuments page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceged/type-document');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeDocument').should('exist');
    cy.url().should('match', typeDocumentPageUrlPattern);
  });

  describe('TypeDocument page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeDocumentPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeDocument page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceged/type-document/new$'));
        cy.getEntityCreateUpdateHeading('TypeDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeDocumentPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceged/api/type-documents',
          body: typeDocumentSample,
        }).then(({ body }) => {
          typeDocument = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceged/api/type-documents+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceged/api/type-documents?page=0&size=20>; rel="last",<http://localhost/services/microserviceged/api/type-documents?page=0&size=20>; rel="first"',
              },
              body: [typeDocument],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeDocumentPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeDocument page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeDocument');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeDocumentPageUrlPattern);
      });

      it('edit button click should load edit TypeDocument page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeDocument');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeDocumentPageUrlPattern);
      });

      it('edit button click should load edit TypeDocument page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeDocument');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeDocumentPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeDocument', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeDocument').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeDocumentPageUrlPattern);

        typeDocument = undefined;
      });
    });
  });

  describe('new TypeDocument page', () => {
    beforeEach(() => {
      cy.visit(`${typeDocumentPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeDocument');
    });

    it('should create an instance of TypeDocument', () => {
      cy.get(`[data-cy="libelleTypeDocument"]`).type('ouf');
      cy.get(`[data-cy="libelleTypeDocument"]`).should('have.value', 'ouf');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeDocument = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeDocumentPageUrlPattern);
    });
  });
});
