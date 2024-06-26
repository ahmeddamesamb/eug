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

describe('Formation e2e test', () => {
  const formationPageUrl = '/microservicegir/formation';
  const formationPageUrlPattern = new RegExp('/microservicegir/formation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formationSample = {};

  let formation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/formations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/formations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/formations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/formations/${formation.id}`,
      }).then(() => {
        formation = undefined;
      });
    }
  });

  it('Formations menu should load Formations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/formation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Formation').should('exist');
    cy.url().should('match', formationPageUrlPattern);
  });

  describe('Formation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Formation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/formation/new$'));
        cy.getEntityCreateUpdateHeading('Formation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/formations',
          body: formationSample,
        }).then(({ body }) => {
          formation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/formations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/formations?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/formations?page=0&size=20>; rel="first"',
              },
              body: [formation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Formation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPageUrlPattern);
      });

      it('edit button click should load edit Formation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Formation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPageUrlPattern);
      });

      it('edit button click should load edit Formation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Formation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPageUrlPattern);
      });

      it('last delete button click should delete instance of Formation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPageUrlPattern);

        formation = undefined;
      });
    });
  });

  describe('new Formation page', () => {
    beforeEach(() => {
      cy.visit(`${formationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Formation');
    });

    it('should create an instance of Formation', () => {
      cy.get(`[data-cy="classeDiplomanteYN"]`).should('not.be.checked');
      cy.get(`[data-cy="classeDiplomanteYN"]`).click();
      cy.get(`[data-cy="classeDiplomanteYN"]`).should('be.checked');

      cy.get(`[data-cy="libelleDiplome"]`).type('partager suivant précisément');
      cy.get(`[data-cy="libelleDiplome"]`).should('have.value', 'partager suivant précisément');

      cy.get(`[data-cy="codeFormation"]`).type('au lieu de par');
      cy.get(`[data-cy="codeFormation"]`).should('have.value', 'au lieu de par');

      cy.get(`[data-cy="nbreCreditsMin"]`).type('11436');
      cy.get(`[data-cy="nbreCreditsMin"]`).should('have.value', '11436');

      cy.get(`[data-cy="estParcoursYN"]`).should('not.be.checked');
      cy.get(`[data-cy="estParcoursYN"]`).click();
      cy.get(`[data-cy="estParcoursYN"]`).should('be.checked');

      cy.get(`[data-cy="lmdYN"]`).should('not.be.checked');
      cy.get(`[data-cy="lmdYN"]`).click();
      cy.get(`[data-cy="lmdYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formationPageUrlPattern);
    });
  });
});
