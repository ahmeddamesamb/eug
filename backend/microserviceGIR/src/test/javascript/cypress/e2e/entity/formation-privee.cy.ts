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

describe('FormationPrivee e2e test', () => {
  const formationPriveePageUrl = '/microservicegir/formation-privee';
  const formationPriveePageUrlPattern = new RegExp('/microservicegir/formation-privee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formationPriveeSample = { nombreMensualites: 2362, coutTotal: 13266.07, mensualite: 15829.59 };

  let formationPrivee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/formation-privees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/formation-privees').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/formation-privees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formationPrivee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/formation-privees/${formationPrivee.id}`,
      }).then(() => {
        formationPrivee = undefined;
      });
    }
  });

  it('FormationPrivees menu should load FormationPrivees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/formation-privee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormationPrivee').should('exist');
    cy.url().should('match', formationPriveePageUrlPattern);
  });

  describe('FormationPrivee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formationPriveePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormationPrivee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/formation-privee/new$'));
        cy.getEntityCreateUpdateHeading('FormationPrivee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPriveePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/formation-privees',
          body: formationPriveeSample,
        }).then(({ body }) => {
          formationPrivee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/formation-privees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/formation-privees?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/formation-privees?page=0&size=20>; rel="first"',
              },
              body: [formationPrivee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formationPriveePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormationPrivee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formationPrivee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPriveePageUrlPattern);
      });

      it('edit button click should load edit FormationPrivee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationPrivee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPriveePageUrlPattern);
      });

      it('edit button click should load edit FormationPrivee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationPrivee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPriveePageUrlPattern);
      });

      it('last delete button click should delete instance of FormationPrivee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formationPrivee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationPriveePageUrlPattern);

        formationPrivee = undefined;
      });
    });
  });

  describe('new FormationPrivee page', () => {
    beforeEach(() => {
      cy.visit(`${formationPriveePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormationPrivee');
    });

    it('should create an instance of FormationPrivee', () => {
      cy.get(`[data-cy="nombreMensualites"]`).type('18031');
      cy.get(`[data-cy="nombreMensualites"]`).should('have.value', '18031');

      cy.get(`[data-cy="paiementPremierMoisYN"]`).should('not.be.checked');
      cy.get(`[data-cy="paiementPremierMoisYN"]`).click();
      cy.get(`[data-cy="paiementPremierMoisYN"]`).should('be.checked');

      cy.get(`[data-cy="paiementDernierMoisYN"]`).should('not.be.checked');
      cy.get(`[data-cy="paiementDernierMoisYN"]`).click();
      cy.get(`[data-cy="paiementDernierMoisYN"]`).should('be.checked');

      cy.get(`[data-cy="fraisDossierYN"]`).should('not.be.checked');
      cy.get(`[data-cy="fraisDossierYN"]`).click();
      cy.get(`[data-cy="fraisDossierYN"]`).should('be.checked');

      cy.get(`[data-cy="coutTotal"]`).type('28351.54');
      cy.get(`[data-cy="coutTotal"]`).should('have.value', '28351.54');

      cy.get(`[data-cy="mensualite"]`).type('18561.05');
      cy.get(`[data-cy="mensualite"]`).should('have.value', '18561.05');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formationPrivee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formationPriveePageUrlPattern);
    });
  });
});
