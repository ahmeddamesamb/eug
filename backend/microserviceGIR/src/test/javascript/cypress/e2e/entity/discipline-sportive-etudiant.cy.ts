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

describe('DisciplineSportiveEtudiant e2e test', () => {
  const disciplineSportiveEtudiantPageUrl = '/microservicegir/discipline-sportive-etudiant';
  const disciplineSportiveEtudiantPageUrlPattern = new RegExp('/microservicegir/discipline-sportive-etudiant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const disciplineSportiveEtudiantSample = {};

  let disciplineSportiveEtudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/discipline-sportive-etudiants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/discipline-sportive-etudiants').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/discipline-sportive-etudiants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (disciplineSportiveEtudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/discipline-sportive-etudiants/${disciplineSportiveEtudiant.id}`,
      }).then(() => {
        disciplineSportiveEtudiant = undefined;
      });
    }
  });

  it('DisciplineSportiveEtudiants menu should load DisciplineSportiveEtudiants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/discipline-sportive-etudiant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DisciplineSportiveEtudiant').should('exist');
    cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
  });

  describe('DisciplineSportiveEtudiant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(disciplineSportiveEtudiantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DisciplineSportiveEtudiant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/discipline-sportive-etudiant/new$'));
        cy.getEntityCreateUpdateHeading('DisciplineSportiveEtudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/discipline-sportive-etudiants',
          body: disciplineSportiveEtudiantSample,
        }).then(({ body }) => {
          disciplineSportiveEtudiant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/discipline-sportive-etudiants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/discipline-sportive-etudiants?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/discipline-sportive-etudiants?page=0&size=20>; rel="first"',
              },
              body: [disciplineSportiveEtudiant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(disciplineSportiveEtudiantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DisciplineSportiveEtudiant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('disciplineSportiveEtudiant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
      });

      it('edit button click should load edit DisciplineSportiveEtudiant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DisciplineSportiveEtudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
      });

      it('edit button click should load edit DisciplineSportiveEtudiant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DisciplineSportiveEtudiant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
      });

      it('last delete button click should delete instance of DisciplineSportiveEtudiant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('disciplineSportiveEtudiant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);

        disciplineSportiveEtudiant = undefined;
      });
    });
  });

  describe('new DisciplineSportiveEtudiant page', () => {
    beforeEach(() => {
      cy.visit(`${disciplineSportiveEtudiantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DisciplineSportiveEtudiant');
    });

    it('should create an instance of DisciplineSportiveEtudiant', () => {
      cy.get(`[data-cy="licenceSportiveYN"]`).should('not.be.checked');
      cy.get(`[data-cy="licenceSportiveYN"]`).click();
      cy.get(`[data-cy="licenceSportiveYN"]`).should('be.checked');

      cy.get(`[data-cy="competitionUGBYN"]`).should('not.be.checked');
      cy.get(`[data-cy="competitionUGBYN"]`).click();
      cy.get(`[data-cy="competitionUGBYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        disciplineSportiveEtudiant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', disciplineSportiveEtudiantPageUrlPattern);
    });
  });
});
