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

describe('Lycee e2e test', () => {
  const lyceePageUrl = '/microservicegir/lycee';
  const lyceePageUrlPattern = new RegExp('/microservicegir/lycee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const lyceeSample = { nomLycee: 'derrière évoluer approcher' };

  let lycee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/lycees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/lycees').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/lycees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (lycee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/lycees/${lycee.id}`,
      }).then(() => {
        lycee = undefined;
      });
    }
  });

  it('Lycees menu should load Lycees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/lycee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Lycee').should('exist');
    cy.url().should('match', lyceePageUrlPattern);
  });

  describe('Lycee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(lyceePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Lycee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/lycee/new$'));
        cy.getEntityCreateUpdateHeading('Lycee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', lyceePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/lycees',
          body: lyceeSample,
        }).then(({ body }) => {
          lycee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/lycees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/lycees?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/lycees?page=0&size=20>; rel="first"',
              },
              body: [lycee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(lyceePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Lycee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('lycee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', lyceePageUrlPattern);
      });

      it('edit button click should load edit Lycee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lycee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', lyceePageUrlPattern);
      });

      it('edit button click should load edit Lycee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Lycee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', lyceePageUrlPattern);
      });

      it('last delete button click should delete instance of Lycee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('lycee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', lyceePageUrlPattern);

        lycee = undefined;
      });
    });
  });

  describe('new Lycee page', () => {
    beforeEach(() => {
      cy.visit(`${lyceePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Lycee');
    });

    it('should create an instance of Lycee', () => {
      cy.get(`[data-cy="nomLycee"]`).type('sale');
      cy.get(`[data-cy="nomLycee"]`).should('have.value', 'sale');

      cy.get(`[data-cy="codeLycee"]`).type('infime jeune enfant');
      cy.get(`[data-cy="codeLycee"]`).should('have.value', 'infime jeune enfant');

      cy.get(`[data-cy="villeLycee"]`).type('plutôt croâ à force de');
      cy.get(`[data-cy="villeLycee"]`).should('have.value', 'plutôt croâ à force de');

      cy.get(`[data-cy="academieLycee"]`).type('23699');
      cy.get(`[data-cy="academieLycee"]`).should('have.value', '23699');

      cy.get(`[data-cy="centreExamen"]`).type("communauté étudiante d'après d'abord");
      cy.get(`[data-cy="centreExamen"]`).should('have.value', "communauté étudiante d'après d'abord");

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        lycee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', lyceePageUrlPattern);
    });
  });
});
