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

describe('Frais e2e test', () => {
  const fraisPageUrl = '/microservicegir/frais';
  const fraisPageUrlPattern = new RegExp('/microservicegir/frais(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fraisSample = { valeurFrais: 14239.64, dateApplication: '2024-06-25', estEnApplicationYN: true };

  let frais;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/frais+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/frais').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/frais/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (frais) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/frais/${frais.id}`,
      }).then(() => {
        frais = undefined;
      });
    }
  });

  it('Frais menu should load Frais page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/frais');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Frais').should('exist');
    cy.url().should('match', fraisPageUrlPattern);
  });

  describe('Frais page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(fraisPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Frais page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/frais/new$'));
        cy.getEntityCreateUpdateHeading('Frais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fraisPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/frais',
          body: fraisSample,
        }).then(({ body }) => {
          frais = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/frais+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/frais?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/frais?page=0&size=20>; rel="first"',
              },
              body: [frais],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(fraisPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Frais page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('frais');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fraisPageUrlPattern);
      });

      it('edit button click should load edit Frais page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Frais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fraisPageUrlPattern);
      });

      it('edit button click should load edit Frais page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Frais');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fraisPageUrlPattern);
      });

      it('last delete button click should delete instance of Frais', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('frais').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', fraisPageUrlPattern);

        frais = undefined;
      });
    });
  });

  describe('new Frais page', () => {
    beforeEach(() => {
      cy.visit(`${fraisPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Frais');
    });

    it('should create an instance of Frais', () => {
      cy.get(`[data-cy="valeurFrais"]`).type('30071.89');
      cy.get(`[data-cy="valeurFrais"]`).should('have.value', '30071.89');

      cy.get(`[data-cy="descriptionFrais"]`).type('si');
      cy.get(`[data-cy="descriptionFrais"]`).should('have.value', 'si');

      cy.get(`[data-cy="fraisPourAssimileYN"]`).should('not.be.checked');
      cy.get(`[data-cy="fraisPourAssimileYN"]`).click();
      cy.get(`[data-cy="fraisPourAssimileYN"]`).should('be.checked');

      cy.get(`[data-cy="fraisPourExonererYN"]`).should('not.be.checked');
      cy.get(`[data-cy="fraisPourExonererYN"]`).click();
      cy.get(`[data-cy="fraisPourExonererYN"]`).should('be.checked');

      cy.get(`[data-cy="dia"]`).type('27134.49');
      cy.get(`[data-cy="dia"]`).should('have.value', '27134.49');

      cy.get(`[data-cy="dip"]`).type('15848.47');
      cy.get(`[data-cy="dip"]`).should('have.value', '15848.47');

      cy.get(`[data-cy="fraisPrivee"]`).type('11834.58');
      cy.get(`[data-cy="fraisPrivee"]`).should('have.value', '11834.58');

      cy.get(`[data-cy="dateApplication"]`).type('2024-06-25');
      cy.get(`[data-cy="dateApplication"]`).blur();
      cy.get(`[data-cy="dateApplication"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="dateFin"]`).type('2024-06-25');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="estEnApplicationYN"]`).should('not.be.checked');
      cy.get(`[data-cy="estEnApplicationYN"]`).click();
      cy.get(`[data-cy="estEnApplicationYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        frais = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', fraisPageUrlPattern);
    });
  });
});
