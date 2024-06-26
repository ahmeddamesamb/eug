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

describe('Ministere e2e test', () => {
  const ministerePageUrl = '/microservicegir/ministere';
  const ministerePageUrlPattern = new RegExp('/microservicegir/ministere(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const ministereSample = { nomMinistere: 'dès que sur', dateDebut: '2024-06-25', enCoursYN: true, actifYN: false };

  let ministere;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/ministeres+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/ministeres').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/ministeres/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ministere) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/ministeres/${ministere.id}`,
      }).then(() => {
        ministere = undefined;
      });
    }
  });

  it('Ministeres menu should load Ministeres page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/ministere');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ministere').should('exist');
    cy.url().should('match', ministerePageUrlPattern);
  });

  describe('Ministere page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(ministerePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ministere page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/ministere/new$'));
        cy.getEntityCreateUpdateHeading('Ministere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministerePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/ministeres',
          body: ministereSample,
        }).then(({ body }) => {
          ministere = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/ministeres+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/ministeres?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/ministeres?page=0&size=20>; rel="first"',
              },
              body: [ministere],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(ministerePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ministere page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ministere');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministerePageUrlPattern);
      });

      it('edit button click should load edit Ministere page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ministere');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministerePageUrlPattern);
      });

      it('edit button click should load edit Ministere page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ministere');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministerePageUrlPattern);
      });

      it('last delete button click should delete instance of Ministere', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('ministere').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', ministerePageUrlPattern);

        ministere = undefined;
      });
    });
  });

  describe('new Ministere page', () => {
    beforeEach(() => {
      cy.visit(`${ministerePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ministere');
    });

    it('should create an instance of Ministere', () => {
      cy.get(`[data-cy="nomMinistere"]`).type('tant que à côté de');
      cy.get(`[data-cy="nomMinistere"]`).should('have.value', 'tant que à côté de');

      cy.get(`[data-cy="sigleMinistere"]`).type('terriblement plic équipe');
      cy.get(`[data-cy="sigleMinistere"]`).should('have.value', 'terriblement plic équipe');

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
        ministere = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', ministerePageUrlPattern);
    });
  });
});
