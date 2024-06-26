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

describe('FormationInvalide e2e test', () => {
  const formationInvalidePageUrl = '/microservicegir/formation-invalide';
  const formationInvalidePageUrlPattern = new RegExp('/microservicegir/formation-invalide(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formationInvalideSample = {};

  let formationInvalide;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/formation-invalides+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/formation-invalides').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/formation-invalides/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formationInvalide) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/formation-invalides/${formationInvalide.id}`,
      }).then(() => {
        formationInvalide = undefined;
      });
    }
  });

  it('FormationInvalides menu should load FormationInvalides page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/formation-invalide');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormationInvalide').should('exist');
    cy.url().should('match', formationInvalidePageUrlPattern);
  });

  describe('FormationInvalide page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formationInvalidePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormationInvalide page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/formation-invalide/new$'));
        cy.getEntityCreateUpdateHeading('FormationInvalide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationInvalidePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/formation-invalides',
          body: formationInvalideSample,
        }).then(({ body }) => {
          formationInvalide = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/formation-invalides+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/formation-invalides?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/formation-invalides?page=0&size=20>; rel="first"',
              },
              body: [formationInvalide],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formationInvalidePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormationInvalide page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formationInvalide');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationInvalidePageUrlPattern);
      });

      it('edit button click should load edit FormationInvalide page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationInvalide');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationInvalidePageUrlPattern);
      });

      it('edit button click should load edit FormationInvalide page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationInvalide');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationInvalidePageUrlPattern);
      });

      it('last delete button click should delete instance of FormationInvalide', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formationInvalide').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationInvalidePageUrlPattern);

        formationInvalide = undefined;
      });
    });
  });

  describe('new FormationInvalide page', () => {
    beforeEach(() => {
      cy.visit(`${formationInvalidePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormationInvalide');
    });

    it('should create an instance of FormationInvalide', () => {
      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formationInvalide = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formationInvalidePageUrlPattern);
    });
  });
});
