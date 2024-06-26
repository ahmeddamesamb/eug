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

describe('PaiementFrais e2e test', () => {
  const paiementFraisPageUrl = '/microservicegir/paiement-frais';
  const paiementFraisPageUrlPattern = new RegExp('/microservicegir/paiement-frais(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementFraisSample = { datePaiement: '2024-06-25' };

  let paiementFrais;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/paiement-frais+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/paiement-frais').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/paiement-frais/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementFrais) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/paiement-frais/${paiementFrais.id}`,
      }).then(() => {
        paiementFrais = undefined;
      });
    }
  });

  it('PaiementFrais menu should load PaiementFrais page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/paiement-frais');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementFrais').should('exist');
    cy.url().should('match', paiementFraisPageUrlPattern);
  });

  describe('PaiementFrais page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementFraisPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementFrais page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/paiement-frais/new$'));
        cy.getEntityCreateUpdateHeading('PaiementFrais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFraisPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/paiement-frais',
          body: paiementFraisSample,
        }).then(({ body }) => {
          paiementFrais = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/paiement-frais+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/paiement-frais?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/paiement-frais?page=0&size=20>; rel="first"',
              },
              body: [paiementFrais],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementFraisPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementFrais page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementFrais');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFraisPageUrlPattern);
      });

      it('edit button click should load edit PaiementFrais page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementFrais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFraisPageUrlPattern);
      });

      it('edit button click should load edit PaiementFrais page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementFrais');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFraisPageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementFrais', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementFrais').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFraisPageUrlPattern);

        paiementFrais = undefined;
      });
    });
  });

  describe('new PaiementFrais page', () => {
    beforeEach(() => {
      cy.visit(`${paiementFraisPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementFrais');
    });

    it('should create an instance of PaiementFrais', () => {
      cy.get(`[data-cy="datePaiement"]`).type('2024-06-25');
      cy.get(`[data-cy="datePaiement"]`).blur();
      cy.get(`[data-cy="datePaiement"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="obligatoireYN"]`).should('not.be.checked');
      cy.get(`[data-cy="obligatoireYN"]`).click();
      cy.get(`[data-cy="obligatoireYN"]`).should('be.checked');

      cy.get(`[data-cy="echeancePayeeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="echeancePayeeYN"]`).click();
      cy.get(`[data-cy="echeancePayeeYN"]`).should('be.checked');

      cy.get(`[data-cy="emailUser"]`).type('zzzz tellement splendide');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'zzzz tellement splendide');

      cy.get(`[data-cy="dateForclos"]`).type('2024-06-25');
      cy.get(`[data-cy="dateForclos"]`).blur();
      cy.get(`[data-cy="dateForclos"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="forclosYN"]`).should('not.be.checked');
      cy.get(`[data-cy="forclosYN"]`).click();
      cy.get(`[data-cy="forclosYN"]`).should('be.checked');

      cy.get(`[data-cy="paimentDelaiYN"]`).should('not.be.checked');
      cy.get(`[data-cy="paimentDelaiYN"]`).click();
      cy.get(`[data-cy="paimentDelaiYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementFrais = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementFraisPageUrlPattern);
    });
  });
});
