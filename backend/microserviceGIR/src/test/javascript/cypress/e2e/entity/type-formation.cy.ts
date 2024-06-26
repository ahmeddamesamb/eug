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

describe('TypeFormation e2e test', () => {
  const typeFormationPageUrl = '/microservicegir/type-formation';
  const typeFormationPageUrlPattern = new RegExp('/microservicegir/type-formation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeFormationSample = { libelleTypeFormation: 'zzzz' };

  let typeFormation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-formations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-formations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-formations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeFormation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-formations/${typeFormation.id}`,
      }).then(() => {
        typeFormation = undefined;
      });
    }
  });

  it('TypeFormations menu should load TypeFormations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-formation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeFormation').should('exist');
    cy.url().should('match', typeFormationPageUrlPattern);
  });

  describe('TypeFormation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeFormationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeFormation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-formation/new$'));
        cy.getEntityCreateUpdateHeading('TypeFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFormationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-formations',
          body: typeFormationSample,
        }).then(({ body }) => {
          typeFormation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-formations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-formations?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-formations?page=0&size=20>; rel="first"',
              },
              body: [typeFormation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeFormationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeFormation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeFormation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFormationPageUrlPattern);
      });

      it('edit button click should load edit TypeFormation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFormationPageUrlPattern);
      });

      it('edit button click should load edit TypeFormation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeFormation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFormationPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeFormation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeFormation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFormationPageUrlPattern);

        typeFormation = undefined;
      });
    });
  });

  describe('new TypeFormation page', () => {
    beforeEach(() => {
      cy.visit(`${typeFormationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeFormation');
    });

    it('should create an instance of TypeFormation', () => {
      cy.get(`[data-cy="libelleTypeFormation"]`).type('ainsi');
      cy.get(`[data-cy="libelleTypeFormation"]`).should('have.value', 'ainsi');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeFormation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeFormationPageUrlPattern);
    });
  });
});
