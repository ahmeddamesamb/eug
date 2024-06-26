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

describe('Candidat e2e test', () => {
  const candidatPageUrl = '/microserviceaclc/candidat';
  const candidatPageUrlPattern = new RegExp('/microserviceaclc/candidat(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const candidatSample = {};

  let candidat;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microserviceaclc/api/candidats+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microserviceaclc/api/candidats').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microserviceaclc/api/candidats/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (candidat) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microserviceaclc/api/candidats/${candidat.id}`,
      }).then(() => {
        candidat = undefined;
      });
    }
  });

  it('Candidats menu should load Candidats page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microserviceaclc/candidat');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Candidat').should('exist');
    cy.url().should('match', candidatPageUrlPattern);
  });

  describe('Candidat page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(candidatPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Candidat page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microserviceaclc/candidat/new$'));
        cy.getEntityCreateUpdateHeading('Candidat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', candidatPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microserviceaclc/api/candidats',
          body: candidatSample,
        }).then(({ body }) => {
          candidat = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microserviceaclc/api/candidats+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microserviceaclc/api/candidats?page=0&size=20>; rel="last",<http://localhost/services/microserviceaclc/api/candidats?page=0&size=20>; rel="first"',
              },
              body: [candidat],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(candidatPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Candidat page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('candidat');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', candidatPageUrlPattern);
      });

      it('edit button click should load edit Candidat page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Candidat');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', candidatPageUrlPattern);
      });

      it('edit button click should load edit Candidat page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Candidat');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', candidatPageUrlPattern);
      });

      it('last delete button click should delete instance of Candidat', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('candidat').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', candidatPageUrlPattern);

        candidat = undefined;
      });
    });
  });

  describe('new Candidat page', () => {
    beforeEach(() => {
      cy.visit(`${candidatPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Candidat');
    });

    it('should create an instance of Candidat', () => {
      cy.get(`[data-cy="nom"]`).type('toc');
      cy.get(`[data-cy="nom"]`).should('have.value', 'toc');

      cy.get(`[data-cy="prenom"]`).type('surprendre');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'surprendre');

      cy.get(`[data-cy="dateNaissance"]`).type('2024-06-25');
      cy.get(`[data-cy="dateNaissance"]`).blur();
      cy.get(`[data-cy="dateNaissance"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="email"]`).type('Anceline_Bertrand@gmail.com');
      cy.get(`[data-cy="email"]`).should('have.value', 'Anceline_Bertrand@gmail.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        candidat = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', candidatPageUrlPattern);
    });
  });
});
