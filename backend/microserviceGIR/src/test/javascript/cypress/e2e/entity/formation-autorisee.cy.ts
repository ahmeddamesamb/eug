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

describe('FormationAutorisee e2e test', () => {
  const formationAutoriseePageUrl = '/microservicegir/formation-autorisee';
  const formationAutoriseePageUrlPattern = new RegExp('/microservicegir/formation-autorisee(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formationAutoriseeSample = {};

  let formationAutorisee;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/formation-autorisees+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/formation-autorisees').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/formation-autorisees/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formationAutorisee) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/formation-autorisees/${formationAutorisee.id}`,
      }).then(() => {
        formationAutorisee = undefined;
      });
    }
  });

  it('FormationAutorisees menu should load FormationAutorisees page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/formation-autorisee');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormationAutorisee').should('exist');
    cy.url().should('match', formationAutoriseePageUrlPattern);
  });

  describe('FormationAutorisee page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formationAutoriseePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormationAutorisee page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/formation-autorisee/new$'));
        cy.getEntityCreateUpdateHeading('FormationAutorisee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationAutoriseePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/formation-autorisees',
          body: formationAutoriseeSample,
        }).then(({ body }) => {
          formationAutorisee = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/formation-autorisees+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/formation-autorisees?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/formation-autorisees?page=0&size=20>; rel="first"',
              },
              body: [formationAutorisee],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formationAutoriseePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormationAutorisee page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formationAutorisee');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationAutoriseePageUrlPattern);
      });

      it('edit button click should load edit FormationAutorisee page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationAutorisee');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationAutoriseePageUrlPattern);
      });

      it('edit button click should load edit FormationAutorisee page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormationAutorisee');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationAutoriseePageUrlPattern);
      });

      it('last delete button click should delete instance of FormationAutorisee', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('formationAutorisee').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formationAutoriseePageUrlPattern);

        formationAutorisee = undefined;
      });
    });
  });

  describe('new FormationAutorisee page', () => {
    beforeEach(() => {
      cy.visit(`${formationAutoriseePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormationAutorisee');
    });

    it('should create an instance of FormationAutorisee', () => {
      cy.get(`[data-cy="dateDebut"]`).type('2024-06-25');
      cy.get(`[data-cy="dateDebut"]`).blur();
      cy.get(`[data-cy="dateDebut"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="dateFin"]`).type('2024-06-25');
      cy.get(`[data-cy="dateFin"]`).blur();
      cy.get(`[data-cy="dateFin"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="enCoursYN"]`).should('not.be.checked');
      cy.get(`[data-cy="enCoursYN"]`).click();
      cy.get(`[data-cy="enCoursYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formationAutorisee = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formationAutoriseePageUrlPattern);
    });
  });
});
