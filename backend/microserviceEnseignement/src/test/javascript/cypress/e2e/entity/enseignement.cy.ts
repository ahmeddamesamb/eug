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

describe('Enseignement e2e test', () => {
  const enseignementPageUrl = '/microserviceenseignement/enseignement';
  const enseignementPageUrlPattern = new RegExp('/microserviceenseignement/enseignement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enseignementSample = {};

  let enseignement;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceenseignement/api/enseignements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceenseignement/api/enseignements').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceenseignement/api/enseignements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enseignement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceenseignement/api/enseignements/${enseignement.id}`,
      }).then(() => {
        enseignement = undefined;
      });
    }
  });

  it('Enseignements menu should load Enseignements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceenseignement/enseignement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enseignement').should('exist');
    cy.url().should('match', enseignementPageUrlPattern);
  });

  describe('Enseignement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enseignementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enseignement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceenseignement/enseignement/new$'));
        cy.getEntityCreateUpdateHeading('Enseignement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceenseignement/api/enseignements',
          body: enseignementSample,
        }).then(({ body }) => {
          enseignement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceenseignement/api/enseignements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceenseignement/api/enseignements?page=0&size=20>; rel="last",<http://localhost/services/microserviceenseignement/api/enseignements?page=0&size=20>; rel="first"',
              },
              body: [enseignement],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enseignementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enseignement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enseignement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignementPageUrlPattern);
      });

      it('edit button click should load edit Enseignement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignementPageUrlPattern);
      });

      it('edit button click should load edit Enseignement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignementPageUrlPattern);
      });

      it('last delete button click should delete instance of Enseignement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('enseignement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignementPageUrlPattern);

        enseignement = undefined;
      });
    });
  });

  describe('new Enseignement page', () => {
    beforeEach(() => {
      cy.visit(`${enseignementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enseignement');
    });

    it('should create an instance of Enseignement', () => {
      cy.get(`[data-cy="libelleEnseignements"]`).type('sincère à peine prestataire de services');
      cy.get(`[data-cy="libelleEnseignements"]`).should('have.value', 'sincère à peine prestataire de services');

      cy.get(`[data-cy="volumeHoraire"]`).type('4940.54');
      cy.get(`[data-cy="volumeHoraire"]`).should('have.value', '4940.54');

      cy.get(`[data-cy="nombreInscrits"]`).type('5660');
      cy.get(`[data-cy="nombreInscrits"]`).should('have.value', '5660');

      cy.get(`[data-cy="groupeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="groupeYN"]`).click();
      cy.get(`[data-cy="groupeYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        enseignement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', enseignementPageUrlPattern);
    });
  });
});
