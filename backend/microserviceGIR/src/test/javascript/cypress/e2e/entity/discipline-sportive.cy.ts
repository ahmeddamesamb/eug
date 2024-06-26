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

describe('DisciplineSportive e2e test', () => {
  const disciplineSportivePageUrl = '/microservicegir/discipline-sportive';
  const disciplineSportivePageUrlPattern = new RegExp('/microservicegir/discipline-sportive(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const disciplineSportiveSample = { libelleDisciplineSportive: 'afin de' };

  let disciplineSportive;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/discipline-sportives+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/discipline-sportives').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/discipline-sportives/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (disciplineSportive) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/discipline-sportives/${disciplineSportive.id}`,
      }).then(() => {
        disciplineSportive = undefined;
      });
    }
  });

  it('DisciplineSportives menu should load DisciplineSportives page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/discipline-sportive');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DisciplineSportive').should('exist');
    cy.url().should('match', disciplineSportivePageUrlPattern);
  });

  describe('DisciplineSportive page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(disciplineSportivePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DisciplineSportive page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/discipline-sportive/new$'));
        cy.getEntityCreateUpdateHeading('DisciplineSportive');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportivePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/discipline-sportives',
          body: disciplineSportiveSample,
        }).then(({ body }) => {
          disciplineSportive = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/discipline-sportives+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/discipline-sportives?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/discipline-sportives?page=0&size=20>; rel="first"',
              },
              body: [disciplineSportive],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(disciplineSportivePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DisciplineSportive page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('disciplineSportive');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportivePageUrlPattern);
      });

      it('edit button click should load edit DisciplineSportive page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DisciplineSportive');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportivePageUrlPattern);
      });

      it('edit button click should load edit DisciplineSportive page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DisciplineSportive');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportivePageUrlPattern);
      });

      it('last delete button click should delete instance of DisciplineSportive', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('disciplineSportive').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportivePageUrlPattern);

        disciplineSportive = undefined;
      });
    });
  });

  describe('new DisciplineSportive page', () => {
    beforeEach(() => {
      cy.visit(`${disciplineSportivePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DisciplineSportive');
    });

    it('should create an instance of DisciplineSportive', () => {
      cy.get(`[data-cy="libelleDisciplineSportive"]`).type('délectable sauf');
      cy.get(`[data-cy="libelleDisciplineSportive"]`).should('have.value', 'délectable sauf');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        disciplineSportive = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', disciplineSportivePageUrlPattern);
    });
  });
});
