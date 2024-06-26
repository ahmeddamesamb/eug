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

describe('InfoUserRessource e2e test', () => {
  const infoUserRessourcePageUrl = '/info-user-ressource';
  const infoUserRessourcePageUrlPattern = new RegExp('/info-user-ressource(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const infoUserRessourceSample = { dateAjout: '2024-06-25', enCoursYN: false };

  let infoUserRessource;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/info-user-ressources+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/info-user-ressources').as('postEntityRequest');
    cy.intercept('DELETE', '/api/info-user-ressources/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (infoUserRessource) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/info-user-ressources/${infoUserRessource.id}`,
      }).then(() => {
        infoUserRessource = undefined;
      });
    }
  });

  it('InfoUserRessources menu should load InfoUserRessources page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('info-user-ressource');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InfoUserRessource').should('exist');
    cy.url().should('match', infoUserRessourcePageUrlPattern);
  });

  describe('InfoUserRessource page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(infoUserRessourcePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InfoUserRessource page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/info-user-ressource/new$'));
        cy.getEntityCreateUpdateHeading('InfoUserRessource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infoUserRessourcePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/info-user-ressources',
          body: infoUserRessourceSample,
        }).then(({ body }) => {
          infoUserRessource = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/info-user-ressources+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/info-user-ressources?page=0&size=20>; rel="last",<http://localhost/api/info-user-ressources?page=0&size=20>; rel="first"',
              },
              body: [infoUserRessource],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(infoUserRessourcePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InfoUserRessource page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('infoUserRessource');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infoUserRessourcePageUrlPattern);
      });

      it('edit button click should load edit InfoUserRessource page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InfoUserRessource');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infoUserRessourcePageUrlPattern);
      });

      it('edit button click should load edit InfoUserRessource page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InfoUserRessource');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infoUserRessourcePageUrlPattern);
      });

      it('last delete button click should delete instance of InfoUserRessource', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('infoUserRessource').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infoUserRessourcePageUrlPattern);

        infoUserRessource = undefined;
      });
    });
  });

  describe('new InfoUserRessource page', () => {
    beforeEach(() => {
      cy.visit(`${infoUserRessourcePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InfoUserRessource');
    });

    it('should create an instance of InfoUserRessource', () => {
      cy.get(`[data-cy="dateAjout"]`).type('2024-06-25');
      cy.get(`[data-cy="dateAjout"]`).blur();
      cy.get(`[data-cy="dateAjout"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="enCoursYN"]`).should('not.be.checked');
      cy.get(`[data-cy="enCoursYN"]`).click();
      cy.get(`[data-cy="enCoursYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        infoUserRessource = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', infoUserRessourcePageUrlPattern);
    });
  });
});
