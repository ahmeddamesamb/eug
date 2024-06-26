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

describe('UserProfileBlocFonctionnel e2e test', () => {
  const userProfileBlocFonctionnelPageUrl = '/user-profile-bloc-fonctionnel';
  const userProfileBlocFonctionnelPageUrlPattern = new RegExp('/user-profile-bloc-fonctionnel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const userProfileBlocFonctionnelSample = { date: '2024-06-25', enCoursYN: true };

  let userProfileBlocFonctionnel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/user-profile-bloc-fonctionnels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/user-profile-bloc-fonctionnels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/user-profile-bloc-fonctionnels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (userProfileBlocFonctionnel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/user-profile-bloc-fonctionnels/${userProfileBlocFonctionnel.id}`,
      }).then(() => {
        userProfileBlocFonctionnel = undefined;
      });
    }
  });

  it('UserProfileBlocFonctionnels menu should load UserProfileBlocFonctionnels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('user-profile-bloc-fonctionnel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('UserProfileBlocFonctionnel').should('exist');
    cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
  });

  describe('UserProfileBlocFonctionnel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(userProfileBlocFonctionnelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create UserProfileBlocFonctionnel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/user-profile-bloc-fonctionnel/new$'));
        cy.getEntityCreateUpdateHeading('UserProfileBlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/user-profile-bloc-fonctionnels',
          body: userProfileBlocFonctionnelSample,
        }).then(({ body }) => {
          userProfileBlocFonctionnel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/user-profile-bloc-fonctionnels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/user-profile-bloc-fonctionnels?page=0&size=20>; rel="last",<http://localhost/api/user-profile-bloc-fonctionnels?page=0&size=20>; rel="first"',
              },
              body: [userProfileBlocFonctionnel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(userProfileBlocFonctionnelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details UserProfileBlocFonctionnel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('userProfileBlocFonctionnel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
      });

      it('edit button click should load edit UserProfileBlocFonctionnel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfileBlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
      });

      it('edit button click should load edit UserProfileBlocFonctionnel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('UserProfileBlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
      });

      it('last delete button click should delete instance of UserProfileBlocFonctionnel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('userProfileBlocFonctionnel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);

        userProfileBlocFonctionnel = undefined;
      });
    });
  });

  describe('new UserProfileBlocFonctionnel page', () => {
    beforeEach(() => {
      cy.visit(`${userProfileBlocFonctionnelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('UserProfileBlocFonctionnel');
    });

    it('should create an instance of UserProfileBlocFonctionnel', () => {
      cy.get(`[data-cy="date"]`).type('2024-06-25');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="enCoursYN"]`).should('not.be.checked');
      cy.get(`[data-cy="enCoursYN"]`).click();
      cy.get(`[data-cy="enCoursYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        userProfileBlocFonctionnel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', userProfileBlocFonctionnelPageUrlPattern);
    });
  });
});
