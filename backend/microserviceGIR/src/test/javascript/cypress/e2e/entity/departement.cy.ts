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

describe('Departement e2e test', () => {
  const departementPageUrl = '/microservicegir/departement';
  const departementPageUrlPattern = new RegExp('/microservicegir/departement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const departementSample = { nomDepatement: 'mairie' };

  let departement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/departements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/departements').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/departements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (departement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/departements/${departement.id}`,
      }).then(() => {
        departement = undefined;
      });
    }
  });

  it('Departements menu should load Departements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/departement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Departement').should('exist');
    cy.url().should('match', departementPageUrlPattern);
  });

  describe('Departement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(departementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Departement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/departement/new$'));
        cy.getEntityCreateUpdateHeading('Departement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/departements',
          body: departementSample,
        }).then(({ body }) => {
          departement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/departements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/departements?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/departements?page=0&size=20>; rel="first"',
              },
              body: [departement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(departementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Departement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('departement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departementPageUrlPattern);
      });

      it('edit button click should load edit Departement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Departement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departementPageUrlPattern);
      });

      it('edit button click should load edit Departement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Departement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departementPageUrlPattern);
      });

      it('last delete button click should delete instance of Departement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('departement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', departementPageUrlPattern);

        departement = undefined;
      });
    });
  });

  describe('new Departement page', () => {
    beforeEach(() => {
      cy.visit(`${departementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Departement');
    });

    it('should create an instance of Departement', () => {
      cy.get(`[data-cy="nomDepatement"]`).type('absolument pisser après que');
      cy.get(`[data-cy="nomDepatement"]`).should('have.value', 'absolument pisser après que');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        departement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', departementPageUrlPattern);
    });
  });
});
