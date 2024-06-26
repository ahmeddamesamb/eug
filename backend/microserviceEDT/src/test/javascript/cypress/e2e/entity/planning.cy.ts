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

describe('Planning e2e test', () => {
  const planningPageUrl = '/microserviceedt/planning';
  const planningPageUrlPattern = new RegExp('/microserviceedt/planning(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const planningSample = {};

  let planning;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceedt/api/plannings+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceedt/api/plannings').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceedt/api/plannings/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (planning) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceedt/api/plannings/${planning.id}`,
      }).then(() => {
        planning = undefined;
      });
    }
  });

  it('Plannings menu should load Plannings page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceedt/planning');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Planning').should('exist');
    cy.url().should('match', planningPageUrlPattern);
  });

  describe('Planning page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(planningPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Planning page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceedt/planning/new$'));
        cy.getEntityCreateUpdateHeading('Planning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', planningPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceedt/api/plannings',
          body: planningSample,
        }).then(({ body }) => {
          planning = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceedt/api/plannings+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceedt/api/plannings?page=0&size=20>; rel="last",<http://localhost/services/microserviceedt/api/plannings?page=0&size=20>; rel="first"',
              },
              body: [planning],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(planningPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Planning page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('planning');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', planningPageUrlPattern);
      });

      it('edit button click should load edit Planning page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Planning');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', planningPageUrlPattern);
      });

      it('edit button click should load edit Planning page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Planning');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', planningPageUrlPattern);
      });

      it('last delete button click should delete instance of Planning', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('planning').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', planningPageUrlPattern);

        planning = undefined;
      });
    });
  });

  describe('new Planning page', () => {
    beforeEach(() => {
      cy.visit(`${planningPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Planning');
    });

    it('should create an instance of Planning', () => {
      cy.get(`[data-cy="dateDebut"]`).type('2024-06-25T11:16');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2024-06-25T11:16');

      cy.get(`[data-cy="dateFin"]`).type('2024-06-25T03:55');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2024-06-25T03:55');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        planning = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', planningPageUrlPattern);
    });
  });
});
