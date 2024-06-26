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

describe('Laboratoire e2e test', () => {
  const laboratoirePageUrl = '/microserviceaua/laboratoire';
  const laboratoirePageUrlPattern = new RegExp('/microserviceaua/laboratoire(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const laboratoireSample = {};

  let laboratoire;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceaua/api/laboratoires+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceaua/api/laboratoires').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceaua/api/laboratoires/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (laboratoire) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceaua/api/laboratoires/${laboratoire.id}`,
      }).then(() => {
        laboratoire = undefined;
      });
    }
  });

  it('Laboratoires menu should load Laboratoires page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceaua/laboratoire');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Laboratoire').should('exist');
    cy.url().should('match', laboratoirePageUrlPattern);
  });

  describe('Laboratoire page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(laboratoirePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Laboratoire page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceaua/laboratoire/new$'));
        cy.getEntityCreateUpdateHeading('Laboratoire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', laboratoirePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceaua/api/laboratoires',
          body: laboratoireSample,
        }).then(({ body }) => {
          laboratoire = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceaua/api/laboratoires+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceaua/api/laboratoires?page=0&size=20>; rel="last",<http://localhost/services/microserviceaua/api/laboratoires?page=0&size=20>; rel="first"',
              },
              body: [laboratoire],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(laboratoirePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Laboratoire page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('laboratoire');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', laboratoirePageUrlPattern);
      });

      it('edit button click should load edit Laboratoire page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Laboratoire');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', laboratoirePageUrlPattern);
      });

      it('edit button click should load edit Laboratoire page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Laboratoire');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', laboratoirePageUrlPattern);
      });

      it('last delete button click should delete instance of Laboratoire', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('laboratoire').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', laboratoirePageUrlPattern);

        laboratoire = undefined;
      });
    });
  });

  describe('new Laboratoire page', () => {
    beforeEach(() => {
      cy.visit(`${laboratoirePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Laboratoire');
    });

    it('should create an instance of Laboratoire', () => {
      cy.get(`[data-cy="nom"]`).type('repousser parmi');
      cy.get(`[data-cy="nom"]`).should('have.value', 'repousser parmi');

      cy.get(`[data-cy="laboratoireCotutelleYN"]`).should('not.be.checked');
      cy.get(`[data-cy="laboratoireCotutelleYN"]`).click();
      cy.get(`[data-cy="laboratoireCotutelleYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        laboratoire = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', laboratoirePageUrlPattern);
    });
  });
});
