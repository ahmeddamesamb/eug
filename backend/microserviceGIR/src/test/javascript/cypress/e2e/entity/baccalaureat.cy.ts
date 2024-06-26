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

describe('Baccalaureat e2e test', () => {
  const baccalaureatPageUrl = '/microservicegir/baccalaureat';
  const baccalaureatPageUrlPattern = new RegExp('/microservicegir/baccalaureat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const baccalaureatSample = {};

  let baccalaureat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/baccalaureats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/baccalaureats').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/baccalaureats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (baccalaureat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/baccalaureats/${baccalaureat.id}`,
      }).then(() => {
        baccalaureat = undefined;
      });
    }
  });

  it('Baccalaureats menu should load Baccalaureats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/baccalaureat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Baccalaureat').should('exist');
    cy.url().should('match', baccalaureatPageUrlPattern);
  });

  describe('Baccalaureat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(baccalaureatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Baccalaureat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/baccalaureat/new$'));
        cy.getEntityCreateUpdateHeading('Baccalaureat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baccalaureatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/baccalaureats',
          body: baccalaureatSample,
        }).then(({ body }) => {
          baccalaureat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/baccalaureats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/baccalaureats?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/baccalaureats?page=0&size=20>; rel="first"',
              },
              body: [baccalaureat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(baccalaureatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Baccalaureat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('baccalaureat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baccalaureatPageUrlPattern);
      });

      it('edit button click should load edit Baccalaureat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Baccalaureat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baccalaureatPageUrlPattern);
      });

      it('edit button click should load edit Baccalaureat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Baccalaureat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baccalaureatPageUrlPattern);
      });

      it('last delete button click should delete instance of Baccalaureat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('baccalaureat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', baccalaureatPageUrlPattern);

        baccalaureat = undefined;
      });
    });
  });

  describe('new Baccalaureat page', () => {
    beforeEach(() => {
      cy.visit(`${baccalaureatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Baccalaureat');
    });

    it('should create an instance of Baccalaureat', () => {
      cy.get(`[data-cy="origineScolaire"]`).type('embarquer population du Québec altruiste');
      cy.get(`[data-cy="origineScolaire"]`).should('have.value', 'embarquer population du Québec altruiste');

      cy.get(`[data-cy="anneeBac"]`).type('2024-06-25');
      cy.get(`[data-cy="anneeBac"]`).blur();
      cy.get(`[data-cy="anneeBac"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="numeroTable"]`).type('18171');
      cy.get(`[data-cy="numeroTable"]`).should('have.value', '18171');

      cy.get(`[data-cy="natureBac"]`).type('raccrocher à même');
      cy.get(`[data-cy="natureBac"]`).should('have.value', 'raccrocher à même');

      cy.get(`[data-cy="mentionBac"]`).type('de peur que dessus');
      cy.get(`[data-cy="mentionBac"]`).should('have.value', 'de peur que dessus');

      cy.get(`[data-cy="moyenneSelectionBac"]`).type('1848.13');
      cy.get(`[data-cy="moyenneSelectionBac"]`).should('have.value', '1848.13');

      cy.get(`[data-cy="moyenneBac"]`).type('10284.96');
      cy.get(`[data-cy="moyenneBac"]`).should('have.value', '10284.96');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        baccalaureat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', baccalaureatPageUrlPattern);
    });
  });
});
