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

describe('Doctorat e2e test', () => {
  const doctoratPageUrl = '/microservicegir/doctorat';
  const doctoratPageUrlPattern = new RegExp('/microservicegir/doctorat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const doctoratSample = { sujet: 'quand dès que quand' };

  let doctorat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/doctorats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/doctorats').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/doctorats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (doctorat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/doctorats/${doctorat.id}`,
      }).then(() => {
        doctorat = undefined;
      });
    }
  });

  it('Doctorats menu should load Doctorats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/doctorat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Doctorat').should('exist');
    cy.url().should('match', doctoratPageUrlPattern);
  });

  describe('Doctorat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(doctoratPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Doctorat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/doctorat/new$'));
        cy.getEntityCreateUpdateHeading('Doctorat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doctoratPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/doctorats',
          body: doctoratSample,
        }).then(({ body }) => {
          doctorat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/doctorats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/doctorats?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/doctorats?page=0&size=20>; rel="first"',
              },
              body: [doctorat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(doctoratPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Doctorat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('doctorat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doctoratPageUrlPattern);
      });

      it('edit button click should load edit Doctorat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Doctorat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doctoratPageUrlPattern);
      });

      it('edit button click should load edit Doctorat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Doctorat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doctoratPageUrlPattern);
      });

      it('last delete button click should delete instance of Doctorat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('doctorat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', doctoratPageUrlPattern);

        doctorat = undefined;
      });
    });
  });

  describe('new Doctorat page', () => {
    beforeEach(() => {
      cy.visit(`${doctoratPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Doctorat');
    });

    it('should create an instance of Doctorat', () => {
      cy.get(`[data-cy="sujet"]`).type('comme');
      cy.get(`[data-cy="sujet"]`).should('have.value', 'comme');

      cy.get(`[data-cy="anneeInscriptionDoctorat"]`).type('2024-06-25');
      cy.get(`[data-cy="anneeInscriptionDoctorat"]`).blur();
      cy.get(`[data-cy="anneeInscriptionDoctorat"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="encadreurId"]`).type('8330');
      cy.get(`[data-cy="encadreurId"]`).should('have.value', '8330');

      cy.get(`[data-cy="laboratoirId"]`).type('7160');
      cy.get(`[data-cy="laboratoirId"]`).should('have.value', '7160');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        doctorat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', doctoratPageUrlPattern);
    });
  });
});
