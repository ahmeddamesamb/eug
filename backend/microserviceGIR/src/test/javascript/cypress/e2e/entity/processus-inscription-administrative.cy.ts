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

describe('ProcessusInscriptionAdministrative e2e test', () => {
  const processusInscriptionAdministrativePageUrl = '/microservicegir/processus-inscription-administrative';
  const processusInscriptionAdministrativePageUrlPattern = new RegExp('/microservicegir/processus-inscription-administrative(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const processusInscriptionAdministrativeSample = {};

  let processusInscriptionAdministrative;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/processus-inscription-administratives+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/processus-inscription-administratives').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/processus-inscription-administratives/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (processusInscriptionAdministrative) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/processus-inscription-administratives/${processusInscriptionAdministrative.id}`,
      }).then(() => {
        processusInscriptionAdministrative = undefined;
      });
    }
  });

  it('ProcessusInscriptionAdministratives menu should load ProcessusInscriptionAdministratives page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/processus-inscription-administrative');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ProcessusInscriptionAdministrative').should('exist');
    cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
  });

  describe('ProcessusInscriptionAdministrative page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(processusInscriptionAdministrativePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ProcessusInscriptionAdministrative page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/processus-inscription-administrative/new$'));
        cy.getEntityCreateUpdateHeading('ProcessusInscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/processus-inscription-administratives',
          body: processusInscriptionAdministrativeSample,
        }).then(({ body }) => {
          processusInscriptionAdministrative = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/processus-inscription-administratives+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/processus-inscription-administratives?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/processus-inscription-administratives?page=0&size=20>; rel="first"',
              },
              body: [processusInscriptionAdministrative],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(processusInscriptionAdministrativePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ProcessusInscriptionAdministrative page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('processusInscriptionAdministrative');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
      });

      it('edit button click should load edit ProcessusInscriptionAdministrative page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProcessusInscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
      });

      it('edit button click should load edit ProcessusInscriptionAdministrative page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ProcessusInscriptionAdministrative');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
      });

      it('last delete button click should delete instance of ProcessusInscriptionAdministrative', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('processusInscriptionAdministrative').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);

        processusInscriptionAdministrative = undefined;
      });
    });
  });

  describe('new ProcessusInscriptionAdministrative page', () => {
    beforeEach(() => {
      cy.visit(`${processusInscriptionAdministrativePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ProcessusInscriptionAdministrative');
    });

    it('should create an instance of ProcessusInscriptionAdministrative', () => {
      cy.get(`[data-cy="inscriptionDemarreeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscriptionDemarreeYN"]`).click();
      cy.get(`[data-cy="inscriptionDemarreeYN"]`).should('be.checked');

      cy.get(`[data-cy="dateDemarrageInscription"]`).type('2024-06-25T14:05');
      cy.get(`[data-cy="dateDemarrageInscription"]`).blur();
      cy.get(`[data-cy="dateDemarrageInscription"]`).should('have.value', '2024-06-25T14:05');

      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).click();
      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).should('be.checked');

      cy.get(`[data-cy="dateAnnulationInscription"]`).type('2024-06-25T11:40');
      cy.get(`[data-cy="dateAnnulationInscription"]`).blur();
      cy.get(`[data-cy="dateAnnulationInscription"]`).should('have.value', '2024-06-25T11:40');

      cy.get(`[data-cy="apteYN"]`).should('not.be.checked');
      cy.get(`[data-cy="apteYN"]`).click();
      cy.get(`[data-cy="apteYN"]`).should('be.checked');

      cy.get(`[data-cy="dateVisiteMedicale"]`).type('2024-06-25T00:31');
      cy.get(`[data-cy="dateVisiteMedicale"]`).blur();
      cy.get(`[data-cy="dateVisiteMedicale"]`).should('have.value', '2024-06-25T00:31');

      cy.get(`[data-cy="beneficiaireCROUSYN"]`).should('not.be.checked');
      cy.get(`[data-cy="beneficiaireCROUSYN"]`).click();
      cy.get(`[data-cy="beneficiaireCROUSYN"]`).should('be.checked');

      cy.get(`[data-cy="dateRemiseQuitusCROUS"]`).type('2024-06-24T23:23');
      cy.get(`[data-cy="dateRemiseQuitusCROUS"]`).blur();
      cy.get(`[data-cy="dateRemiseQuitusCROUS"]`).should('have.value', '2024-06-24T23:23');

      cy.get(`[data-cy="nouveauCodeBUYN"]`).should('not.be.checked');
      cy.get(`[data-cy="nouveauCodeBUYN"]`).click();
      cy.get(`[data-cy="nouveauCodeBUYN"]`).should('be.checked');

      cy.get(`[data-cy="quitusBUYN"]`).should('not.be.checked');
      cy.get(`[data-cy="quitusBUYN"]`).click();
      cy.get(`[data-cy="quitusBUYN"]`).should('be.checked');

      cy.get(`[data-cy="dateRemiseQuitusBU"]`).type('2024-06-25T07:18');
      cy.get(`[data-cy="dateRemiseQuitusBU"]`).blur();
      cy.get(`[data-cy="dateRemiseQuitusBU"]`).should('have.value', '2024-06-25T07:18');

      cy.get(`[data-cy="exporterBUYN"]`).should('not.be.checked');
      cy.get(`[data-cy="exporterBUYN"]`).click();
      cy.get(`[data-cy="exporterBUYN"]`).should('be.checked');

      cy.get(`[data-cy="fraisObligatoiresPayesYN"]`).should('not.be.checked');
      cy.get(`[data-cy="fraisObligatoiresPayesYN"]`).click();
      cy.get(`[data-cy="fraisObligatoiresPayesYN"]`).should('be.checked');

      cy.get(`[data-cy="datePaiementFraisObligatoires"]`).type('2024-06-25T15:03');
      cy.get(`[data-cy="datePaiementFraisObligatoires"]`).blur();
      cy.get(`[data-cy="datePaiementFraisObligatoires"]`).should('have.value', '2024-06-25T15:03');

      cy.get(`[data-cy="numeroQuitusCROUS"]`).type('18015');
      cy.get(`[data-cy="numeroQuitusCROUS"]`).should('have.value', '18015');

      cy.get(`[data-cy="carteEturemiseYN"]`).should('not.be.checked');
      cy.get(`[data-cy="carteEturemiseYN"]`).click();
      cy.get(`[data-cy="carteEturemiseYN"]`).should('be.checked');

      cy.get(`[data-cy="carteDupremiseYN"]`).should('not.be.checked');
      cy.get(`[data-cy="carteDupremiseYN"]`).click();
      cy.get(`[data-cy="carteDupremiseYN"]`).should('be.checked');

      cy.get(`[data-cy="dateRemiseCarteEtu"]`).type('2024-06-25T00:56');
      cy.get(`[data-cy="dateRemiseCarteEtu"]`).blur();
      cy.get(`[data-cy="dateRemiseCarteEtu"]`).should('have.value', '2024-06-25T00:56');

      cy.get(`[data-cy="dateRemiseCarteDup"]`).type('21019');
      cy.get(`[data-cy="dateRemiseCarteDup"]`).should('have.value', '21019');

      cy.get(`[data-cy="inscritAdministrativementYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscritAdministrativementYN"]`).click();
      cy.get(`[data-cy="inscritAdministrativementYN"]`).should('be.checked');

      cy.get(`[data-cy="dateInscriptionAdministrative"]`).type('2024-06-25T11:31');
      cy.get(`[data-cy="dateInscriptionAdministrative"]`).blur();
      cy.get(`[data-cy="dateInscriptionAdministrative"]`).should('have.value', '2024-06-25T11:31');

      cy.get(`[data-cy="derniereModification"]`).type('2024-06-25T05:33');
      cy.get(`[data-cy="derniereModification"]`).blur();
      cy.get(`[data-cy="derniereModification"]`).should('have.value', '2024-06-25T05:33');

      cy.get(`[data-cy="inscritOnlineYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscritOnlineYN"]`).click();
      cy.get(`[data-cy="inscritOnlineYN"]`).should('be.checked');

      cy.get(`[data-cy="emailUser"]`).type('équipe de recherche cocorico a');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'équipe de recherche cocorico a');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        processusInscriptionAdministrative = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', processusInscriptionAdministrativePageUrlPattern);
    });
  });
});
