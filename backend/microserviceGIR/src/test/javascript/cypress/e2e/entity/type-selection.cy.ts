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

describe('TypeSelection e2e test', () => {
  const typeSelectionPageUrl = '/microservicegir/type-selection';
  const typeSelectionPageUrlPattern = new RegExp('/microservicegir/type-selection(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeSelectionSample = { libelleTypeSelection: 'ouin' };

  let typeSelection;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-selections+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-selections').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-selections/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeSelection) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-selections/${typeSelection.id}`,
      }).then(() => {
        typeSelection = undefined;
      });
    }
  });

  it('TypeSelections menu should load TypeSelections page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-selection');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeSelection').should('exist');
    cy.url().should('match', typeSelectionPageUrlPattern);
  });

  describe('TypeSelection page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeSelectionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeSelection page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-selection/new$'));
        cy.getEntityCreateUpdateHeading('TypeSelection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeSelectionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-selections',
          body: typeSelectionSample,
        }).then(({ body }) => {
          typeSelection = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-selections+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-selections?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-selections?page=0&size=20>; rel="first"',
              },
              body: [typeSelection],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeSelectionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeSelection page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeSelection');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeSelectionPageUrlPattern);
      });

      it('edit button click should load edit TypeSelection page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeSelection');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeSelectionPageUrlPattern);
      });

      it('edit button click should load edit TypeSelection page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeSelection');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeSelectionPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeSelection', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeSelection').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeSelectionPageUrlPattern);

        typeSelection = undefined;
      });
    });
  });

  describe('new TypeSelection page', () => {
    beforeEach(() => {
      cy.visit(`${typeSelectionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeSelection');
    });

    it('should create an instance of TypeSelection', () => {
      cy.get(`[data-cy="libelleTypeSelection"]`).type('de façon à ce que oups');
      cy.get(`[data-cy="libelleTypeSelection"]`).should('have.value', 'de façon à ce que oups');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeSelection = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeSelectionPageUrlPattern);
    });
  });
});
