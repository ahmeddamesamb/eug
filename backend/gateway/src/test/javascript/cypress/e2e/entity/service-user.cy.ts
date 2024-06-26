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

describe('ServiceUser e2e test', () => {
  const serviceUserPageUrl = '/service-user';
  const serviceUserPageUrlPattern = new RegExp('/service-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const serviceUserSample = { nom: 'après jeune', dateAjout: '2024-06-25', actifYN: true };

  let serviceUser;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/service-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/service-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/service-users/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serviceUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-users/${serviceUser.id}`,
      }).then(() => {
        serviceUser = undefined;
      });
    }
  });

  it('ServiceUsers menu should load ServiceUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('service-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServiceUser').should('exist');
    cy.url().should('match', serviceUserPageUrlPattern);
  });

  describe('ServiceUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serviceUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServiceUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/service-user/new$'));
        cy.getEntityCreateUpdateHeading('ServiceUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/service-users',
          body: serviceUserSample,
        }).then(({ body }) => {
          serviceUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/service-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/service-users?page=0&size=20>; rel="last",<http://localhost/api/service-users?page=0&size=20>; rel="first"',
              },
              body: [serviceUser],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(serviceUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServiceUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serviceUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceUserPageUrlPattern);
      });

      it('edit button click should load edit ServiceUser page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceUserPageUrlPattern);
      });

      it('edit button click should load edit ServiceUser page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceUser');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceUserPageUrlPattern);
      });

      it('last delete button click should delete instance of ServiceUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('serviceUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceUserPageUrlPattern);

        serviceUser = undefined;
      });
    });
  });

  describe('new ServiceUser page', () => {
    beforeEach(() => {
      cy.visit(`${serviceUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServiceUser');
    });

    it('should create an instance of ServiceUser', () => {
      cy.get(`[data-cy="nom"]`).type('virer cocorico');
      cy.get(`[data-cy="nom"]`).should('have.value', 'virer cocorico');

      cy.get(`[data-cy="dateAjout"]`).type('2024-06-24');
      cy.get(`[data-cy="dateAjout"]`).blur();
      cy.get(`[data-cy="dateAjout"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        serviceUser = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', serviceUserPageUrlPattern);
    });
  });
});
