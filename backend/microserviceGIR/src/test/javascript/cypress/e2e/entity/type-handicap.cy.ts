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

describe('TypeHandicap e2e test', () => {
  const typeHandicapPageUrl = '/microservicegir/type-handicap';
  const typeHandicapPageUrlPattern = new RegExp('/microservicegir/type-handicap(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeHandicapSample = { libelleTypeHandicap: 'chez encourager' };

  let typeHandicap;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-handicaps+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-handicaps').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-handicaps/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeHandicap) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-handicaps/${typeHandicap.id}`,
      }).then(() => {
        typeHandicap = undefined;
      });
    }
  });

  it('TypeHandicaps menu should load TypeHandicaps page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-handicap');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeHandicap').should('exist');
    cy.url().should('match', typeHandicapPageUrlPattern);
  });

  describe('TypeHandicap page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeHandicapPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeHandicap page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-handicap/new$'));
        cy.getEntityCreateUpdateHeading('TypeHandicap');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeHandicapPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-handicaps',
          body: typeHandicapSample,
        }).then(({ body }) => {
          typeHandicap = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-handicaps+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-handicaps?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-handicaps?page=0&size=20>; rel="first"',
              },
              body: [typeHandicap],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeHandicapPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeHandicap page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeHandicap');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeHandicapPageUrlPattern);
      });

      it('edit button click should load edit TypeHandicap page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeHandicap');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeHandicapPageUrlPattern);
      });

      it('edit button click should load edit TypeHandicap page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeHandicap');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeHandicapPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeHandicap', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeHandicap').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeHandicapPageUrlPattern);

        typeHandicap = undefined;
      });
    });
  });

  describe('new TypeHandicap page', () => {
    beforeEach(() => {
      cy.visit(`${typeHandicapPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeHandicap');
    });

    it('should create an instance of TypeHandicap', () => {
      cy.get(`[data-cy="libelleTypeHandicap"]`).type('désormais financer');
      cy.get(`[data-cy="libelleTypeHandicap"]`).should('have.value', 'désormais financer');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeHandicap = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeHandicapPageUrlPattern);
    });
  });
});
