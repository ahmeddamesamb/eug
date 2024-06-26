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

describe('Operateur e2e test', () => {
  const operateurPageUrl = '/microservicegir/operateur';
  const operateurPageUrlPattern = new RegExp('/microservicegir/operateur(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const operateurSample = { libelleOperateur: 'au-dehors', userLogin: 'quand', codeOperateur: 'consentir percer tellement' };

  let operateur;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/operateurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/operateurs').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/operateurs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (operateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/operateurs/${operateur.id}`,
      }).then(() => {
        operateur = undefined;
      });
    }
  });

  it('Operateurs menu should load Operateurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/operateur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Operateur').should('exist');
    cy.url().should('match', operateurPageUrlPattern);
  });

  describe('Operateur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(operateurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Operateur page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/operateur/new$'));
        cy.getEntityCreateUpdateHeading('Operateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/operateurs',
          body: operateurSample,
        }).then(({ body }) => {
          operateur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/operateurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/operateurs?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/operateurs?page=0&size=20>; rel="first"',
              },
              body: [operateur],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(operateurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Operateur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('operateur');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });

      it('edit button click should load edit Operateur page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });

      it('edit button click should load edit Operateur page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operateur');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });

      it('last delete button click should delete instance of Operateur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('operateur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);

        operateur = undefined;
      });
    });
  });

  describe('new Operateur page', () => {
    beforeEach(() => {
      cy.visit(`${operateurPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Operateur');
    });

    it('should create an instance of Operateur', () => {
      cy.get(`[data-cy="libelleOperateur"]`).type('vide');
      cy.get(`[data-cy="libelleOperateur"]`).should('have.value', 'vide');

      cy.get(`[data-cy="userLogin"]`).type('pourvu que avex guide');
      cy.get(`[data-cy="userLogin"]`).should('have.value', 'pourvu que avex guide');

      cy.get(`[data-cy="codeOperateur"]`).type('trop peu propre');
      cy.get(`[data-cy="codeOperateur"]`).should('have.value', 'trop peu propre');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        operateur = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', operateurPageUrlPattern);
    });
  });
});
