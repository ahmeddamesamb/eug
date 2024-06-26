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

describe('Universite e2e test', () => {
  const universitePageUrl = '/microservicegir/universite';
  const universitePageUrlPattern = new RegExp('/microservicegir/universite(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const universiteSample = { nomUniversite: 'blablabla', sigleUniversite: 'maintenant timide défendre', actifYN: true };

  let universite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/universites+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/universites').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/universites/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (universite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/universites/${universite.id}`,
      }).then(() => {
        universite = undefined;
      });
    }
  });

  it('Universites menu should load Universites page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/universite');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Universite').should('exist');
    cy.url().should('match', universitePageUrlPattern);
  });

  describe('Universite page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(universitePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Universite page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/universite/new$'));
        cy.getEntityCreateUpdateHeading('Universite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', universitePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/universites',
          body: universiteSample,
        }).then(({ body }) => {
          universite = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/universites+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/universites?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/universites?page=0&size=20>; rel="first"',
              },
              body: [universite],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(universitePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Universite page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('universite');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', universitePageUrlPattern);
      });

      it('edit button click should load edit Universite page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Universite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', universitePageUrlPattern);
      });

      it('edit button click should load edit Universite page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Universite');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', universitePageUrlPattern);
      });

      it('last delete button click should delete instance of Universite', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('universite').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', universitePageUrlPattern);

        universite = undefined;
      });
    });
  });

  describe('new Universite page', () => {
    beforeEach(() => {
      cy.visit(`${universitePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Universite');
    });

    it('should create an instance of Universite', () => {
      cy.get(`[data-cy="nomUniversite"]`).type('groin groin énorme');
      cy.get(`[data-cy="nomUniversite"]`).should('have.value', 'groin groin énorme');

      cy.get(`[data-cy="sigleUniversite"]`).type('drelin');
      cy.get(`[data-cy="sigleUniversite"]`).should('have.value', 'drelin');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        universite = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', universitePageUrlPattern);
    });
  });
});
