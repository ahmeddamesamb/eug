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

describe('Serie e2e test', () => {
  const seriePageUrl = '/microservicegir/serie';
  const seriePageUrlPattern = new RegExp('/microservicegir/serie(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const serieSample = { libelleSerie: 'rassurer avare', sigleSerie: 'débattre retracer ah' };

  let serie;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/series+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/series').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/series/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serie) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/series/${serie.id}`,
      }).then(() => {
        serie = undefined;
      });
    }
  });

  it('Series menu should load Series page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/serie');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Serie').should('exist');
    cy.url().should('match', seriePageUrlPattern);
  });

  describe('Serie page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(seriePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Serie page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/serie/new$'));
        cy.getEntityCreateUpdateHeading('Serie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seriePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/series',
          body: serieSample,
        }).then(({ body }) => {
          serie = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/series+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/series?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/series?page=0&size=20>; rel="first"',
              },
              body: [serie],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(seriePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Serie page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serie');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seriePageUrlPattern);
      });

      it('edit button click should load edit Serie page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Serie');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seriePageUrlPattern);
      });

      it('edit button click should load edit Serie page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Serie');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seriePageUrlPattern);
      });

      it('last delete button click should delete instance of Serie', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('serie').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', seriePageUrlPattern);

        serie = undefined;
      });
    });
  });

  describe('new Serie page', () => {
    beforeEach(() => {
      cy.visit(`${seriePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Serie');
    });

    it('should create an instance of Serie', () => {
      cy.get(`[data-cy="codeSerie"]`).type('de sorte que outre comme');
      cy.get(`[data-cy="codeSerie"]`).should('have.value', 'de sorte que outre comme');

      cy.get(`[data-cy="libelleSerie"]`).type('personnel');
      cy.get(`[data-cy="libelleSerie"]`).should('have.value', 'personnel');

      cy.get(`[data-cy="sigleSerie"]`).type("près de manière à ce que d'avec");
      cy.get(`[data-cy="sigleSerie"]`).should('have.value', "près de manière à ce que d'avec");

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        serie = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', seriePageUrlPattern);
    });
  });
});
