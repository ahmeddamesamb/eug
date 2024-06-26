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

describe('Campagne e2e test', () => {
  const campagnePageUrl = '/microservicegir/campagne';
  const campagnePageUrlPattern = new RegExp('/microservicegir/campagne(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const campagneSample = {};

  let campagne;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/campagnes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/campagnes').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/campagnes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (campagne) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/campagnes/${campagne.id}`,
      }).then(() => {
        campagne = undefined;
      });
    }
  });

  it('Campagnes menu should load Campagnes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/campagne');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Campagne').should('exist');
    cy.url().should('match', campagnePageUrlPattern);
  });

  describe('Campagne page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(campagnePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Campagne page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/campagne/new$'));
        cy.getEntityCreateUpdateHeading('Campagne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', campagnePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/campagnes',
          body: campagneSample,
        }).then(({ body }) => {
          campagne = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/campagnes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/campagnes?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/campagnes?page=0&size=20>; rel="first"',
              },
              body: [campagne],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(campagnePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Campagne page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('campagne');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', campagnePageUrlPattern);
      });

      it('edit button click should load edit Campagne page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Campagne');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', campagnePageUrlPattern);
      });

      it('edit button click should load edit Campagne page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Campagne');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', campagnePageUrlPattern);
      });

      it('last delete button click should delete instance of Campagne', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('campagne').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', campagnePageUrlPattern);

        campagne = undefined;
      });
    });
  });

  describe('new Campagne page', () => {
    beforeEach(() => {
      cy.visit(`${campagnePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Campagne');
    });

    it('should create an instance of Campagne', () => {
      cy.get(`[data-cy="libelleCampagne"]`).type('après que');
      cy.get(`[data-cy="libelleCampagne"]`).should('have.value', 'après que');

      cy.get(`[data-cy="dateDebut"]`).type('2024-06-25');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="dateFin"]`).type('2024-06-25');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="libelleAbrege"]`).type('grâce à');
      cy.get(`[data-cy="libelleAbrege"]`).should('have.value', 'grâce à');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        campagne = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', campagnePageUrlPattern);
    });
  });
});
