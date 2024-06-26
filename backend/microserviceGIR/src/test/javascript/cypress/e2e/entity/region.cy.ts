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

describe('Region e2e test', () => {
  const regionPageUrl = '/microservicegir/region';
  const regionPageUrlPattern = new RegExp('/microservicegir/region(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const regionSample = { libelleRegion: 'ouille depuis' };

  let region;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/regions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/regions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/regions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (region) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/regions/${region.id}`,
      }).then(() => {
        region = undefined;
      });
    }
  });

  it('Regions menu should load Regions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/region');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Region').should('exist');
    cy.url().should('match', regionPageUrlPattern);
  });

  describe('Region page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(regionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Region page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/region/new$'));
        cy.getEntityCreateUpdateHeading('Region');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/regions',
          body: regionSample,
        }).then(({ body }) => {
          region = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/regions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/regions?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/regions?page=0&size=20>; rel="first"',
              },
              body: [region],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(regionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Region page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('region');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regionPageUrlPattern);
      });

      it('edit button click should load edit Region page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Region');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regionPageUrlPattern);
      });

      it('edit button click should load edit Region page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Region');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regionPageUrlPattern);
      });

      it('last delete button click should delete instance of Region', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('region').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', regionPageUrlPattern);

        region = undefined;
      });
    });
  });

  describe('new Region page', () => {
    beforeEach(() => {
      cy.visit(`${regionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Region');
    });

    it('should create an instance of Region', () => {
      cy.get(`[data-cy="libelleRegion"]`).type('hé jeune enfant');
      cy.get(`[data-cy="libelleRegion"]`).should('have.value', 'hé jeune enfant');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        region = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', regionPageUrlPattern);
    });
  });
});
