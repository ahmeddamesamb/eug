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

describe('BlocFonctionnel e2e test', () => {
  const blocFonctionnelPageUrl = '/bloc-fonctionnel';
  const blocFonctionnelPageUrlPattern = new RegExp('/bloc-fonctionnel(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const blocFonctionnelSample = { libelleBloc: 'meuh', dateAjoutBloc: '2024-06-25', actifYN: true };

  let blocFonctionnel;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bloc-fonctionnels+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bloc-fonctionnels').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bloc-fonctionnels/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (blocFonctionnel) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bloc-fonctionnels/${blocFonctionnel.id}`,
      }).then(() => {
        blocFonctionnel = undefined;
      });
    }
  });

  it('BlocFonctionnels menu should load BlocFonctionnels page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bloc-fonctionnel');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BlocFonctionnel').should('exist');
    cy.url().should('match', blocFonctionnelPageUrlPattern);
  });

  describe('BlocFonctionnel page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(blocFonctionnelPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BlocFonctionnel page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bloc-fonctionnel/new$'));
        cy.getEntityCreateUpdateHeading('BlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', blocFonctionnelPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bloc-fonctionnels',
          body: blocFonctionnelSample,
        }).then(({ body }) => {
          blocFonctionnel = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bloc-fonctionnels+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/bloc-fonctionnels?page=0&size=20>; rel="last",<http://localhost/api/bloc-fonctionnels?page=0&size=20>; rel="first"',
              },
              body: [blocFonctionnel],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(blocFonctionnelPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BlocFonctionnel page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('blocFonctionnel');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', blocFonctionnelPageUrlPattern);
      });

      it('edit button click should load edit BlocFonctionnel page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', blocFonctionnelPageUrlPattern);
      });

      it('edit button click should load edit BlocFonctionnel page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BlocFonctionnel');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', blocFonctionnelPageUrlPattern);
      });

      it('last delete button click should delete instance of BlocFonctionnel', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('blocFonctionnel').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', blocFonctionnelPageUrlPattern);

        blocFonctionnel = undefined;
      });
    });
  });

  describe('new BlocFonctionnel page', () => {
    beforeEach(() => {
      cy.visit(`${blocFonctionnelPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BlocFonctionnel');
    });

    it('should create an instance of BlocFonctionnel', () => {
      cy.get(`[data-cy="libelleBloc"]`).type('vaste pendant rectorat');
      cy.get(`[data-cy="libelleBloc"]`).should('have.value', 'vaste pendant rectorat');

      cy.get(`[data-cy="dateAjoutBloc"]`).type('2024-06-24');
      cy.get(`[data-cy="dateAjoutBloc"]`).blur();
      cy.get(`[data-cy="dateAjoutBloc"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        blocFonctionnel = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', blocFonctionnelPageUrlPattern);
    });
  });
});
