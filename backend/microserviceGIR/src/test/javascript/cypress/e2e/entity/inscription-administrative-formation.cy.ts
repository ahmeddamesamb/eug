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

describe('InscriptionAdministrativeFormation e2e test', () => {
  const inscriptionAdministrativeFormationPageUrl = '/microservicegir/inscription-administrative-formation';
  const inscriptionAdministrativeFormationPageUrlPattern = new RegExp('/microservicegir/inscription-administrative-formation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const inscriptionAdministrativeFormationSample = { exonereYN: true };

  let inscriptionAdministrativeFormation;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/inscription-administrative-formations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/inscription-administrative-formations').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/inscription-administrative-formations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (inscriptionAdministrativeFormation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/inscription-administrative-formations/${inscriptionAdministrativeFormation.id}`,
      }).then(() => {
        inscriptionAdministrativeFormation = undefined;
      });
    }
  });

  it('InscriptionAdministrativeFormations menu should load InscriptionAdministrativeFormations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/inscription-administrative-formation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InscriptionAdministrativeFormation').should('exist');
    cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
  });

  describe('InscriptionAdministrativeFormation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(inscriptionAdministrativeFormationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InscriptionAdministrativeFormation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/inscription-administrative-formation/new$'));
        cy.getEntityCreateUpdateHeading('InscriptionAdministrativeFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/inscription-administrative-formations',
          body: inscriptionAdministrativeFormationSample,
        }).then(({ body }) => {
          inscriptionAdministrativeFormation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/inscription-administrative-formations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/inscription-administrative-formations?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/inscription-administrative-formations?page=0&size=20>; rel="first"',
              },
              body: [inscriptionAdministrativeFormation],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(inscriptionAdministrativeFormationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InscriptionAdministrativeFormation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('inscriptionAdministrativeFormation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
      });

      it('edit button click should load edit InscriptionAdministrativeFormation page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionAdministrativeFormation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
      });

      it('edit button click should load edit InscriptionAdministrativeFormation page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InscriptionAdministrativeFormation');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
      });

      it('last delete button click should delete instance of InscriptionAdministrativeFormation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('inscriptionAdministrativeFormation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);

        inscriptionAdministrativeFormation = undefined;
      });
    });
  });

  describe('new InscriptionAdministrativeFormation page', () => {
    beforeEach(() => {
      cy.visit(`${inscriptionAdministrativeFormationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InscriptionAdministrativeFormation');
    });

    it('should create an instance of InscriptionAdministrativeFormation', () => {
      cy.get(`[data-cy="inscriptionPrincipaleYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscriptionPrincipaleYN"]`).click();
      cy.get(`[data-cy="inscriptionPrincipaleYN"]`).should('be.checked');

      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).should('not.be.checked');
      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).click();
      cy.get(`[data-cy="inscriptionAnnuleeYN"]`).should('be.checked');

      cy.get(`[data-cy="exonereYN"]`).should('not.be.checked');
      cy.get(`[data-cy="exonereYN"]`).click();
      cy.get(`[data-cy="exonereYN"]`).should('be.checked');

      cy.get(`[data-cy="paiementFraisOblYN"]`).should('not.be.checked');
      cy.get(`[data-cy="paiementFraisOblYN"]`).click();
      cy.get(`[data-cy="paiementFraisOblYN"]`).should('be.checked');

      cy.get(`[data-cy="paiementFraisIntegergYN"]`).should('not.be.checked');
      cy.get(`[data-cy="paiementFraisIntegergYN"]`).click();
      cy.get(`[data-cy="paiementFraisIntegergYN"]`).should('be.checked');

      cy.get(`[data-cy="certificatDelivreYN"]`).should('not.be.checked');
      cy.get(`[data-cy="certificatDelivreYN"]`).click();
      cy.get(`[data-cy="certificatDelivreYN"]`).should('be.checked');

      cy.get(`[data-cy="dateChoixFormation"]`).type('2024-06-25T05:23');
      cy.get(`[data-cy="dateChoixFormation"]`).blur();
      cy.get(`[data-cy="dateChoixFormation"]`).should('have.value', '2024-06-25T05:23');

      cy.get(`[data-cy="dateValidationInscription"]`).type('2024-06-25T18:14');
      cy.get(`[data-cy="dateValidationInscription"]`).blur();
      cy.get(`[data-cy="dateValidationInscription"]`).should('have.value', '2024-06-25T18:14');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        inscriptionAdministrativeFormation = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', inscriptionAdministrativeFormationPageUrlPattern);
    });
  });
});
