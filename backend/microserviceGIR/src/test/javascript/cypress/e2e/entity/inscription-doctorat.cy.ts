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

describe('InscriptionDoctorat e2e test', () => {
  const inscriptionDoctoratPageUrl = '/microservicegir/inscription-doctorat';
  const inscriptionDoctoratPageUrlPattern = new RegExp('/microservicegir/inscription-doctorat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inscriptionDoctoratSample = {};

  let inscriptionDoctorat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/inscription-doctorats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/inscription-doctorats').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/inscription-doctorats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inscriptionDoctorat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/inscription-doctorats/${inscriptionDoctorat.id}`,
      }).then(() => {
        inscriptionDoctorat = undefined;
      });
    }
  });

  it('InscriptionDoctorats menu should load InscriptionDoctorats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/inscription-doctorat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InscriptionDoctorat').should('exist');
    cy.url().should('match', inscriptionDoctoratPageUrlPattern);
  });

  describe('InscriptionDoctorat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inscriptionDoctoratPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InscriptionDoctorat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/inscription-doctorat/new$'));
        cy.getEntityCreateUpdateHeading('InscriptionDoctorat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionDoctoratPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/inscription-doctorats',
          body: inscriptionDoctoratSample,
        }).then(({ body }) => {
          inscriptionDoctorat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/inscription-doctorats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/inscription-doctorats?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/inscription-doctorats?page=0&size=20>; rel="first"',
              },
              body: [inscriptionDoctorat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inscriptionDoctoratPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InscriptionDoctorat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inscriptionDoctorat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionDoctoratPageUrlPattern);
      });

      it('edit button click should load edit InscriptionDoctorat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionDoctorat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionDoctoratPageUrlPattern);
      });

      it('edit button click should load edit InscriptionDoctorat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionDoctorat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionDoctoratPageUrlPattern);
      });

      it('last delete button click should delete instance of InscriptionDoctorat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inscriptionDoctorat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionDoctoratPageUrlPattern);

        inscriptionDoctorat = undefined;
      });
    });
  });

  describe('new InscriptionDoctorat page', () => {
    beforeEach(() => {
      cy.visit(`${inscriptionDoctoratPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InscriptionDoctorat');
    });

    it('should create an instance of InscriptionDoctorat', () => {
      cy.get(`[data-cy="sourceFinancement"]`).type('toc quasi pourvu que');
      cy.get(`[data-cy="sourceFinancement"]`).should('have.value', 'toc quasi pourvu que');

      cy.get(`[data-cy="coEncadreurId"]`).type('assez');
      cy.get(`[data-cy="coEncadreurId"]`).should('have.value', 'assez');

      cy.get(`[data-cy="nombreInscription"]`).type('5296');
      cy.get(`[data-cy="nombreInscription"]`).should('have.value', '5296');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inscriptionDoctorat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inscriptionDoctoratPageUrlPattern);
    });
  });
});
