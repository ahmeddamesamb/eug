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

describe('TypeFrais e2e test', () => {
  const typeFraisPageUrl = '/microservicegir/type-frais';
  const typeFraisPageUrlPattern = new RegExp('/microservicegir/type-frais(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeFraisSample = { libelleTypeFrais: 'conseil d’administration' };

  let typeFrais;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-frais+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-frais').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-frais/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeFrais) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-frais/${typeFrais.id}`,
      }).then(() => {
        typeFrais = undefined;
      });
    }
  });

  it('TypeFrais menu should load TypeFrais page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-frais');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeFrais').should('exist');
    cy.url().should('match', typeFraisPageUrlPattern);
  });

  describe('TypeFrais page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeFraisPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeFrais page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-frais/new$'));
        cy.getEntityCreateUpdateHeading('TypeFrais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFraisPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-frais',
          body: typeFraisSample,
        }).then(({ body }) => {
          typeFrais = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-frais+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-frais?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-frais?page=0&size=20>; rel="first"',
              },
              body: [typeFrais],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeFraisPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeFrais page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeFrais');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFraisPageUrlPattern);
      });

      it('edit button click should load edit TypeFrais page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeFrais');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFraisPageUrlPattern);
      });

      it('edit button click should load edit TypeFrais page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeFrais');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFraisPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeFrais', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeFrais').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeFraisPageUrlPattern);

        typeFrais = undefined;
      });
    });
  });

  describe('new TypeFrais page', () => {
    beforeEach(() => {
      cy.visit(`${typeFraisPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeFrais');
    });

    it('should create an instance of TypeFrais', () => {
      cy.get(`[data-cy="libelleTypeFrais"]`).type('au dépens de encourager');
      cy.get(`[data-cy="libelleTypeFrais"]`).should('have.value', 'au dépens de encourager');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeFrais = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeFraisPageUrlPattern);
    });
  });
});
