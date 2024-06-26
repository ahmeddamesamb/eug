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

describe('Specialite e2e test', () => {
  const specialitePageUrl = '/microservicegir/specialite';
  const specialitePageUrlPattern = new RegExp('/microservicegir/specialite(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const specialiteSample = { nomSpecialites: 'placide ah aussi', sigleSpecialites: 'spécialiste', specialitesPayanteYN: true };

  let specialite;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/specialites+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/specialites').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/specialites/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (specialite) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/specialites/${specialite.id}`,
      }).then(() => {
        specialite = undefined;
      });
    }
  });

  it('Specialites menu should load Specialites page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/specialite');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Specialite').should('exist');
    cy.url().should('match', specialitePageUrlPattern);
  });

  describe('Specialite page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(specialitePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Specialite page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/specialite/new$'));
        cy.getEntityCreateUpdateHeading('Specialite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialitePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/specialites',
          body: specialiteSample,
        }).then(({ body }) => {
          specialite = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/specialites+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/specialites?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/specialites?page=0&size=20>; rel="first"',
              },
              body: [specialite],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(specialitePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Specialite page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('specialite');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialitePageUrlPattern);
      });

      it('edit button click should load edit Specialite page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Specialite');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialitePageUrlPattern);
      });

      it('edit button click should load edit Specialite page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Specialite');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialitePageUrlPattern);
      });

      it('last delete button click should delete instance of Specialite', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('specialite').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', specialitePageUrlPattern);

        specialite = undefined;
      });
    });
  });

  describe('new Specialite page', () => {
    beforeEach(() => {
      cy.visit(`${specialitePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Specialite');
    });

    it('should create an instance of Specialite', () => {
      cy.get(`[data-cy="nomSpecialites"]`).type('svelte hebdomadaire');
      cy.get(`[data-cy="nomSpecialites"]`).should('have.value', 'svelte hebdomadaire');

      cy.get(`[data-cy="sigleSpecialites"]`).type('en plus de');
      cy.get(`[data-cy="sigleSpecialites"]`).should('have.value', 'en plus de');

      cy.get(`[data-cy="specialiteParticulierYN"]`).should('not.be.checked');
      cy.get(`[data-cy="specialiteParticulierYN"]`).click();
      cy.get(`[data-cy="specialiteParticulierYN"]`).should('be.checked');

      cy.get(`[data-cy="specialitesPayanteYN"]`).should('not.be.checked');
      cy.get(`[data-cy="specialitesPayanteYN"]`).click();
      cy.get(`[data-cy="specialitesPayanteYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        specialite = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', specialitePageUrlPattern);
    });
  });
});
