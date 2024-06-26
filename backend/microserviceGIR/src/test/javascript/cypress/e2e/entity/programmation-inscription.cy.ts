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

describe('ProgrammationInscription e2e test', () => {
  const programmationInscriptionPageUrl = '/microservicegir/programmation-inscription';
  const programmationInscriptionPageUrlPattern = new RegExp('/microservicegir/programmation-inscription(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const programmationInscriptionSample = {};

  let programmationInscription;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/programmation-inscriptions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/programmation-inscriptions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/programmation-inscriptions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (programmationInscription) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/programmation-inscriptions/${programmationInscription.id}`,
      }).then(() => {
        programmationInscription = undefined;
      });
    }
  });

  it('ProgrammationInscriptions menu should load ProgrammationInscriptions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/programmation-inscription');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProgrammationInscription').should('exist');
    cy.url().should('match', programmationInscriptionPageUrlPattern);
  });

  describe('ProgrammationInscription page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(programmationInscriptionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProgrammationInscription page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/programmation-inscription/new$'));
        cy.getEntityCreateUpdateHeading('ProgrammationInscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', programmationInscriptionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/programmation-inscriptions',
          body: programmationInscriptionSample,
        }).then(({ body }) => {
          programmationInscription = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/programmation-inscriptions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/programmation-inscriptions?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/programmation-inscriptions?page=0&size=20>; rel="first"',
              },
              body: [programmationInscription],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(programmationInscriptionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProgrammationInscription page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('programmationInscription');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', programmationInscriptionPageUrlPattern);
      });

      it('edit button click should load edit ProgrammationInscription page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProgrammationInscription');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', programmationInscriptionPageUrlPattern);
      });

      it('edit button click should load edit ProgrammationInscription page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProgrammationInscription');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', programmationInscriptionPageUrlPattern);
      });

      it('last delete button click should delete instance of ProgrammationInscription', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('programmationInscription').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', programmationInscriptionPageUrlPattern);

        programmationInscription = undefined;
      });
    });
  });

  describe('new ProgrammationInscription page', () => {
    beforeEach(() => {
      cy.visit(`${programmationInscriptionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProgrammationInscription');
    });

    it('should create an instance of ProgrammationInscription', () => {
      cy.get(`[data-cy="libelleProgrammation"]`).type('concurrence mince ruiner');
      cy.get(`[data-cy="libelleProgrammation"]`).should('have.value', 'concurrence mince ruiner');

      cy.get(`[data-cy="dateDebutProgrammation"]`).type('2024-06-25');
      cy.get(`[data-cy="dateDebutProgrammation"]`).blur();
      cy.get(`[data-cy="dateDebutProgrammation"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="dateFinProgrammation"]`).type('2024-06-25');
      cy.get(`[data-cy="dateFinProgrammation"]`).blur();
      cy.get(`[data-cy="dateFinProgrammation"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="ouvertYN"]`).should('not.be.checked');
      cy.get(`[data-cy="ouvertYN"]`).click();
      cy.get(`[data-cy="ouvertYN"]`).should('be.checked');

      cy.get(`[data-cy="emailUser"]`).type('de manière à ce que séculaire chez');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'de manière à ce que séculaire chez');

      cy.get(`[data-cy="dateForclosClasse"]`).type('2024-06-24');
      cy.get(`[data-cy="dateForclosClasse"]`).blur();
      cy.get(`[data-cy="dateForclosClasse"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="forclosClasseYN"]`).should('not.be.checked');
      cy.get(`[data-cy="forclosClasseYN"]`).click();
      cy.get(`[data-cy="forclosClasseYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        programmationInscription = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', programmationInscriptionPageUrlPattern);
    });
  });
});
