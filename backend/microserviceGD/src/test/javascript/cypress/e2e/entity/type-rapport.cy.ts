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

describe('TypeRapport e2e test', () => {
  const typeRapportPageUrl = '/microservicegd/type-rapport';
  const typeRapportPageUrlPattern = new RegExp('/microservicegd/type-rapport(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeRapportSample = {};

  let typeRapport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegd/api/type-rapports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegd/api/type-rapports').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegd/api/type-rapports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeRapport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegd/api/type-rapports/${typeRapport.id}`,
      }).then(() => {
        typeRapport = undefined;
      });
    }
  });

  it('TypeRapports menu should load TypeRapports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegd/type-rapport');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeRapport').should('exist');
    cy.url().should('match', typeRapportPageUrlPattern);
  });

  describe('TypeRapport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeRapportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeRapport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegd/type-rapport/new$'));
        cy.getEntityCreateUpdateHeading('TypeRapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeRapportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegd/api/type-rapports',
          body: typeRapportSample,
        }).then(({ body }) => {
          typeRapport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegd/api/type-rapports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegd/api/type-rapports?page=0&size=20>; rel="last",<http://localhost/services/microservicegd/api/type-rapports?page=0&size=20>; rel="first"',
              },
              body: [typeRapport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeRapportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeRapport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeRapport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeRapportPageUrlPattern);
      });

      it('edit button click should load edit TypeRapport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeRapport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeRapportPageUrlPattern);
      });

      it('edit button click should load edit TypeRapport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeRapport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeRapportPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeRapport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeRapport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeRapportPageUrlPattern);

        typeRapport = undefined;
      });
    });
  });

  describe('new TypeRapport page', () => {
    beforeEach(() => {
      cy.visit(`${typeRapportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeRapport');
    });

    it('should create an instance of TypeRapport', () => {
      cy.get(`[data-cy="libelleTypeRapport"]`).type('spécialiste clac');
      cy.get(`[data-cy="libelleTypeRapport"]`).should('have.value', 'spécialiste clac');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeRapport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeRapportPageUrlPattern);
    });
  });
});
