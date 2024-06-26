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

describe('Cycle e2e test', () => {
  const cyclePageUrl = '/microservicegir/cycle';
  const cyclePageUrlPattern = new RegExp('/microservicegir/cycle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const cycleSample = { libelleCycle: 'ouch malgré' };

  let cycle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/cycles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/cycles').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/cycles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (cycle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/cycles/${cycle.id}`,
      }).then(() => {
        cycle = undefined;
      });
    }
  });

  it('Cycles menu should load Cycles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/cycle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Cycle').should('exist');
    cy.url().should('match', cyclePageUrlPattern);
  });

  describe('Cycle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(cyclePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Cycle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/cycle/new$'));
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/cycles',
          body: cycleSample,
        }).then(({ body }) => {
          cycle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/cycles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/cycles?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/cycles?page=0&size=20>; rel="first"',
              },
              body: [cycle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(cyclePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Cycle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('cycle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      it('edit button click should load edit Cycle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      it('edit button click should load edit Cycle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Cycle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);
      });

      it('last delete button click should delete instance of Cycle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('cycle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', cyclePageUrlPattern);

        cycle = undefined;
      });
    });
  });

  describe('new Cycle page', () => {
    beforeEach(() => {
      cy.visit(`${cyclePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Cycle');
    });

    it('should create an instance of Cycle', () => {
      cy.get(`[data-cy="libelleCycle"]`).type('masquer police mairie');
      cy.get(`[data-cy="libelleCycle"]`).should('have.value', 'masquer police mairie');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        cycle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', cyclePageUrlPattern);
    });
  });
});
