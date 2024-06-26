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

describe('DocumentDelivre e2e test', () => {
  const documentDelivrePageUrl = '/microserviceged/document-delivre';
  const documentDelivrePageUrlPattern = new RegExp('/microserviceged/document-delivre(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const documentDelivreSample = {};

  let documentDelivre;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceged/api/document-delivres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceged/api/document-delivres').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceged/api/document-delivres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (documentDelivre) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceged/api/document-delivres/${documentDelivre.id}`,
      }).then(() => {
        documentDelivre = undefined;
      });
    }
  });

  it('DocumentDelivres menu should load DocumentDelivres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceged/document-delivre');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DocumentDelivre').should('exist');
    cy.url().should('match', documentDelivrePageUrlPattern);
  });

  describe('DocumentDelivre page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(documentDelivrePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DocumentDelivre page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceged/document-delivre/new$'));
        cy.getEntityCreateUpdateHeading('DocumentDelivre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentDelivrePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceged/api/document-delivres',
          body: documentDelivreSample,
        }).then(({ body }) => {
          documentDelivre = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceged/api/document-delivres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceged/api/document-delivres?page=0&size=20>; rel="last",<http://localhost/services/microserviceged/api/document-delivres?page=0&size=20>; rel="first"',
              },
              body: [documentDelivre],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(documentDelivrePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DocumentDelivre page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('documentDelivre');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentDelivrePageUrlPattern);
      });

      it('edit button click should load edit DocumentDelivre page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentDelivre');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentDelivrePageUrlPattern);
      });

      it('edit button click should load edit DocumentDelivre page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DocumentDelivre');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentDelivrePageUrlPattern);
      });

      it('last delete button click should delete instance of DocumentDelivre', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('documentDelivre').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', documentDelivrePageUrlPattern);

        documentDelivre = undefined;
      });
    });
  });

  describe('new DocumentDelivre page', () => {
    beforeEach(() => {
      cy.visit(`${documentDelivrePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DocumentDelivre');
    });

    it('should create an instance of DocumentDelivre', () => {
      cy.get(`[data-cy="libelleDoc"]`).type('étant donné que');
      cy.get(`[data-cy="libelleDoc"]`).should('have.value', 'étant donné que');

      cy.get(`[data-cy="anneeDoc"]`).type('2024-06-25T08:25');
      cy.get(`[data-cy="anneeDoc"]`).blur();
      cy.get(`[data-cy="anneeDoc"]`).should('have.value', '2024-06-25T08:25');

      cy.get(`[data-cy="dateEnregistrement"]`).type('2024-06-24T22:14');
      cy.get(`[data-cy="dateEnregistrement"]`).blur();
      cy.get(`[data-cy="dateEnregistrement"]`).should('have.value', '2024-06-24T22:14');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        documentDelivre = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', documentDelivrePageUrlPattern);
    });
  });
});
