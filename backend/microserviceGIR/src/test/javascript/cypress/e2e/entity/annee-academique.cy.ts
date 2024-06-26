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

describe('AnneeAcademique e2e test', () => {
  const anneeAcademiquePageUrl = '/microservicegir/annee-academique';
  const anneeAcademiquePageUrlPattern = new RegExp('/microservicegir/annee-academique(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const anneeAcademiqueSample = { libelleAnneeAcademique: 'résonner', anneeAc: 2028, separateur: 'a' };

  let anneeAcademique;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/annee-academiques+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/annee-academiques').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/annee-academiques/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (anneeAcademique) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/annee-academiques/${anneeAcademique.id}`,
      }).then(() => {
        anneeAcademique = undefined;
      });
    }
  });

  it('AnneeAcademiques menu should load AnneeAcademiques page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/annee-academique');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('AnneeAcademique').should('exist');
    cy.url().should('match', anneeAcademiquePageUrlPattern);
  });

  describe('AnneeAcademique page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(anneeAcademiquePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create AnneeAcademique page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/annee-academique/new$'));
        cy.getEntityCreateUpdateHeading('AnneeAcademique');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', anneeAcademiquePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/annee-academiques',
          body: anneeAcademiqueSample,
        }).then(({ body }) => {
          anneeAcademique = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/annee-academiques+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/annee-academiques?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/annee-academiques?page=0&size=20>; rel="first"',
              },
              body: [anneeAcademique],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(anneeAcademiquePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details AnneeAcademique page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('anneeAcademique');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', anneeAcademiquePageUrlPattern);
      });

      it('edit button click should load edit AnneeAcademique page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnneeAcademique');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', anneeAcademiquePageUrlPattern);
      });

      it('edit button click should load edit AnneeAcademique page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('AnneeAcademique');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', anneeAcademiquePageUrlPattern);
      });

      it('last delete button click should delete instance of AnneeAcademique', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('anneeAcademique').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', anneeAcademiquePageUrlPattern);

        anneeAcademique = undefined;
      });
    });
  });

  describe('new AnneeAcademique page', () => {
    beforeEach(() => {
      cy.visit(`${anneeAcademiquePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('AnneeAcademique');
    });

    it('should create an instance of AnneeAcademique', () => {
      cy.get(`[data-cy="libelleAnneeAcademique"]`).type('broum triathlète');
      cy.get(`[data-cy="libelleAnneeAcademique"]`).should('have.value', 'broum triathlète');

      cy.get(`[data-cy="anneeAc"]`).type('1995');
      cy.get(`[data-cy="anneeAc"]`).should('have.value', '1995');

      cy.get(`[data-cy="separateur"]`).type('p');
      cy.get(`[data-cy="separateur"]`).should('have.value', 'p');

      cy.get(`[data-cy="anneeCouranteYN"]`).should('not.be.checked');
      cy.get(`[data-cy="anneeCouranteYN"]`).click();
      cy.get(`[data-cy="anneeCouranteYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        anneeAcademique = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', anneeAcademiquePageUrlPattern);
    });
  });
});
