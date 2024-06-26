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

describe('InfosUser e2e test', () => {
  const infosUserPageUrl = '/infos-user';
  const infosUserPageUrlPattern = new RegExp('/infos-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const infosUserSample = { dateAjout: '2024-06-25', actifYN: false };

  let infosUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/infos-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/infos-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/infos-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (infosUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/infos-users/${infosUser.id}`,
      }).then(() => {
        infosUser = undefined;
      });
    }
  });

  it('InfosUsers menu should load InfosUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('infos-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InfosUser').should('exist');
    cy.url().should('match', infosUserPageUrlPattern);
  });

  describe('InfosUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(infosUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InfosUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/infos-user/new$'));
        cy.getEntityCreateUpdateHeading('InfosUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infosUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/infos-users',
          body: infosUserSample,
        }).then(({ body }) => {
          infosUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/infos-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/infos-users?page=0&size=20>; rel="last",<http://localhost/api/infos-users?page=0&size=20>; rel="first"',
              },
              body: [infosUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(infosUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InfosUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('infosUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infosUserPageUrlPattern);
      });

      it('edit button click should load edit InfosUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InfosUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infosUserPageUrlPattern);
      });

      it('edit button click should load edit InfosUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InfosUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infosUserPageUrlPattern);
      });

      it('last delete button click should delete instance of InfosUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('infosUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', infosUserPageUrlPattern);

        infosUser = undefined;
      });
    });
  });

  describe('new InfosUser page', () => {
    beforeEach(() => {
      cy.visit(`${infosUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InfosUser');
    });

    it('should create an instance of InfosUser', () => {
      cy.get(`[data-cy="dateAjout"]`).type('2024-06-25');
      cy.get(`[data-cy="dateAjout"]`).blur();
      cy.get(`[data-cy="dateAjout"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        infosUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', infosUserPageUrlPattern);
    });
  });
});
