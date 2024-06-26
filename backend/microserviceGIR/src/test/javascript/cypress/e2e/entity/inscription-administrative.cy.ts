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

describe('InscriptionAdministrative e2e test', () => {
  const inscriptionAdministrativePageUrl = '/microservicegir/inscription-administrative';
  const inscriptionAdministrativePageUrlPattern = new RegExp('/microservicegir/inscription-administrative(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inscriptionAdministrativeSample = {};

  let inscriptionAdministrative;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/inscription-administratives+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/inscription-administratives').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/inscription-administratives/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inscriptionAdministrative) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/inscription-administratives/${inscriptionAdministrative.id}`,
      }).then(() => {
        inscriptionAdministrative = undefined;
      });
    }
  });

  it('InscriptionAdministratives menu should load InscriptionAdministratives page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/inscription-administrative');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InscriptionAdministrative').should('exist');
    cy.url().should('match', inscriptionAdministrativePageUrlPattern);
  });

  describe('InscriptionAdministrative page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inscriptionAdministrativePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InscriptionAdministrative page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/inscription-administrative/new$'));
        cy.getEntityCreateUpdateHeading('InscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/inscription-administratives',
          body: inscriptionAdministrativeSample,
        }).then(({ body }) => {
          inscriptionAdministrative = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/inscription-administratives+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/inscription-administratives?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/inscription-administratives?page=0&size=20>; rel="first"',
              },
              body: [inscriptionAdministrative],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inscriptionAdministrativePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InscriptionAdministrative page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inscriptionAdministrative');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativePageUrlPattern);
      });

      it('edit button click should load edit InscriptionAdministrative page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativePageUrlPattern);
      });

      it('edit button click should load edit InscriptionAdministrative page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativePageUrlPattern);
      });

      it('last delete button click should delete instance of InscriptionAdministrative', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inscriptionAdministrative').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativePageUrlPattern);

        inscriptionAdministrative = undefined;
      });
    });
  });

  describe('new InscriptionAdministrative page', () => {
    beforeEach(() => {
      cy.visit(`${inscriptionAdministrativePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InscriptionAdministrative');
    });

    it('should create an instance of InscriptionAdministrative', () => {
      cy.get(`[data-cy="nouveauInscritYN"]`).should('not.be.checked');
      cy.get(`[data-cy="nouveauInscritYN"]`).click();
      cy.get(`[data-cy="nouveauInscritYN"]`).should('be.checked');

      cy.get(`[data-cy="repriseYN"]`).should('not.be.checked');
      cy.get(`[data-cy="repriseYN"]`).click();
      cy.get(`[data-cy="repriseYN"]`).should('be.checked');

      cy.get(`[data-cy="autoriseYN"]`).should('not.be.checked');
      cy.get(`[data-cy="autoriseYN"]`).click();
      cy.get(`[data-cy="autoriseYN"]`).should('be.checked');

      cy.get(`[data-cy="ordreInscription"]`).type('4569');
      cy.get(`[data-cy="ordreInscription"]`).should('have.value', '4569');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inscriptionAdministrative = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inscriptionAdministrativePageUrlPattern);
    });
  });
});
