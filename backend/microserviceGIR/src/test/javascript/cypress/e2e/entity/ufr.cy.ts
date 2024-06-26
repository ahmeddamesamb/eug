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

describe('Ufr e2e test', () => {
  const ufrPageUrl = '/microservicegir/ufr';
  const ufrPageUrlPattern = new RegExp('/microservicegir/ufr(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ufrSample = { libelleUfr: 'commissionnaire', sigleUfr: 'avant de patientèle' };

  let ufr;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/ufrs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/ufrs').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/ufrs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ufr) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/ufrs/${ufr.id}`,
      }).then(() => {
        ufr = undefined;
      });
    }
  });

  it('Ufrs menu should load Ufrs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/ufr');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ufr').should('exist');
    cy.url().should('match', ufrPageUrlPattern);
  });

  describe('Ufr page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ufrPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ufr page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/ufr/new$'));
        cy.getEntityCreateUpdateHeading('Ufr');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ufrPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/ufrs',
          body: ufrSample,
        }).then(({ body }) => {
          ufr = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/ufrs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/ufrs?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/ufrs?page=0&size=20>; rel="first"',
              },
              body: [ufr],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ufrPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ufr page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ufr');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ufrPageUrlPattern);
      });

      it('edit button click should load edit Ufr page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ufr');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ufrPageUrlPattern);
      });

      it('edit button click should load edit Ufr page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ufr');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ufrPageUrlPattern);
      });

      it('last delete button click should delete instance of Ufr', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ufr').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ufrPageUrlPattern);

        ufr = undefined;
      });
    });
  });

  describe('new Ufr page', () => {
    beforeEach(() => {
      cy.visit(`${ufrPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ufr');
    });

    it('should create an instance of Ufr', () => {
      cy.get(`[data-cy="libelleUfr"]`).type("terne d'abord oups");
      cy.get(`[data-cy="libelleUfr"]`).should('have.value', "terne d'abord oups");

      cy.get(`[data-cy="sigleUfr"]`).type('concurrence intrépide de façon à ce que');
      cy.get(`[data-cy="sigleUfr"]`).should('have.value', 'concurrence intrépide de façon à ce que');

      cy.get(`[data-cy="prefixe"]`).type('offrir si');
      cy.get(`[data-cy="prefixe"]`).should('have.value', 'offrir si');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ufr = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ufrPageUrlPattern);
    });
  });
});
