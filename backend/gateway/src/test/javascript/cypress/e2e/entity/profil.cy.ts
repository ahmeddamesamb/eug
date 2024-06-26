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

describe('Profil e2e test', () => {
  const profilPageUrl = '/profil';
  const profilPageUrlPattern = new RegExp('/profil(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const profilSample = { libelle: 'du moment que remonter marron', dateAjout: '2024-06-24', actifYN: true };

  let profil;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/profils+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/profils').as('postEntityRequest');
    cy.intercept('DELETE', '/api/profils/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (profil) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/profils/${profil.id}`,
      }).then(() => {
        profil = undefined;
      });
    }
  });

  it('Profils menu should load Profils page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('profil');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Profil').should('exist');
    cy.url().should('match', profilPageUrlPattern);
  });

  describe('Profil page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(profilPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Profil page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/profil/new$'));
        cy.getEntityCreateUpdateHeading('Profil');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', profilPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/profils',
          body: profilSample,
        }).then(({ body }) => {
          profil = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/profils+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/profils?page=0&size=20>; rel="last",<http://localhost/api/profils?page=0&size=20>; rel="first"',
              },
              body: [profil],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(profilPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Profil page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('profil');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', profilPageUrlPattern);
      });

      it('edit button click should load edit Profil page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Profil');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', profilPageUrlPattern);
      });

      it('edit button click should load edit Profil page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Profil');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', profilPageUrlPattern);
      });

      it('last delete button click should delete instance of Profil', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('profil').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', profilPageUrlPattern);

        profil = undefined;
      });
    });
  });

  describe('new Profil page', () => {
    beforeEach(() => {
      cy.visit(`${profilPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Profil');
    });

    it('should create an instance of Profil', () => {
      cy.get(`[data-cy="libelle"]`).type('vlan');
      cy.get(`[data-cy="libelle"]`).should('have.value', 'vlan');

      cy.get(`[data-cy="dateAjout"]`).type('2024-06-25');
      cy.get(`[data-cy="dateAjout"]`).blur();
      cy.get(`[data-cy="dateAjout"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        profil = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', profilPageUrlPattern);
    });
  });
});
