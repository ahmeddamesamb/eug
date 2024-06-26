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

describe('PaiementFormationPrivee e2e test', () => {
  const paiementFormationPriveePageUrl = '/microservicegir/paiement-formation-privee';
  const paiementFormationPriveePageUrlPattern = new RegExp('/microservicegir/paiement-formation-privee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paiementFormationPriveeSample = {};

  let paiementFormationPrivee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/paiement-formation-privees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/paiement-formation-privees').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/paiement-formation-privees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paiementFormationPrivee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/paiement-formation-privees/${paiementFormationPrivee.id}`,
      }).then(() => {
        paiementFormationPrivee = undefined;
      });
    }
  });

  it('PaiementFormationPrivees menu should load PaiementFormationPrivees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/paiement-formation-privee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaiementFormationPrivee').should('exist');
    cy.url().should('match', paiementFormationPriveePageUrlPattern);
  });

  describe('PaiementFormationPrivee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paiementFormationPriveePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaiementFormationPrivee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/paiement-formation-privee/new$'));
        cy.getEntityCreateUpdateHeading('PaiementFormationPrivee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFormationPriveePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/paiement-formation-privees',
          body: paiementFormationPriveeSample,
        }).then(({ body }) => {
          paiementFormationPrivee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/paiement-formation-privees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/paiement-formation-privees?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/paiement-formation-privees?page=0&size=20>; rel="first"',
              },
              body: [paiementFormationPrivee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(paiementFormationPriveePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaiementFormationPrivee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paiementFormationPrivee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFormationPriveePageUrlPattern);
      });

      it('edit button click should load edit PaiementFormationPrivee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementFormationPrivee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFormationPriveePageUrlPattern);
      });

      it('edit button click should load edit PaiementFormationPrivee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaiementFormationPrivee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFormationPriveePageUrlPattern);
      });

      it('last delete button click should delete instance of PaiementFormationPrivee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paiementFormationPrivee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', paiementFormationPriveePageUrlPattern);

        paiementFormationPrivee = undefined;
      });
    });
  });

  describe('new PaiementFormationPrivee page', () => {
    beforeEach(() => {
      cy.visit(`${paiementFormationPriveePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaiementFormationPrivee');
    });

    it('should create an instance of PaiementFormationPrivee', () => {
      cy.get(`[data-cy="datePaiement"]`).type('2024-06-25T10:20');
      cy.get(`[data-cy="datePaiement"]`).blur();
      cy.get(`[data-cy="datePaiement"]`).should('have.value', '2024-06-25T10:20');

      cy.get(`[data-cy="moisPaiement"]`).type('membre à vie');
      cy.get(`[data-cy="moisPaiement"]`).should('have.value', 'membre à vie');

      cy.get(`[data-cy="anneePaiement"]`).type('avant');
      cy.get(`[data-cy="anneePaiement"]`).should('have.value', 'avant');

      cy.get(`[data-cy="payerMensualiteYN"]`).should('not.be.checked');
      cy.get(`[data-cy="payerMensualiteYN"]`).click();
      cy.get(`[data-cy="payerMensualiteYN"]`).should('be.checked');

      cy.get(`[data-cy="payerDelaisYN"]`).should('not.be.checked');
      cy.get(`[data-cy="payerDelaisYN"]`).click();
      cy.get(`[data-cy="payerDelaisYN"]`).should('be.checked');

      cy.get(`[data-cy="emailUser"]`).type('bzzz');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'bzzz');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        paiementFormationPrivee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', paiementFormationPriveePageUrlPattern);
    });
  });
});
