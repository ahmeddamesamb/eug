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

describe('Etudiant e2e test', () => {
  const etudiantPageUrl = '/microservicegir/etudiant';
  const etudiantPageUrlPattern = new RegExp('/microservicegir/etudiant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const etudiantSample = {
    codeEtu: 'vraiment de crainte que',
    dateNaissEtu: '2024-06-25',
    lieuNaissEtu: 'sombre tellement puisque',
    sexe: 'triathlète réjouir croâ',
    assimileYN: false,
  };

  let etudiant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/etudiants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/etudiants').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/etudiants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (etudiant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/etudiants/${etudiant.id}`,
      }).then(() => {
        etudiant = undefined;
      });
    }
  });

  it('Etudiants menu should load Etudiants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/etudiant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Etudiant').should('exist');
    cy.url().should('match', etudiantPageUrlPattern);
  });

  describe('Etudiant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(etudiantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Etudiant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/etudiant/new$'));
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/etudiants',
          body: etudiantSample,
        }).then(({ body }) => {
          etudiant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/etudiants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/etudiants?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/etudiants?page=0&size=20>; rel="first"',
              },
              body: [etudiant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(etudiantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Etudiant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('etudiant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('edit button click should load edit Etudiant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('edit button click should load edit Etudiant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Etudiant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);
      });

      it('last delete button click should delete instance of Etudiant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('etudiant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', etudiantPageUrlPattern);

        etudiant = undefined;
      });
    });
  });

  describe('new Etudiant page', () => {
    beforeEach(() => {
      cy.visit(`${etudiantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Etudiant');
    });

    it('should create an instance of Etudiant', () => {
      cy.get(`[data-cy="codeEtu"]`).type('ouch novice');
      cy.get(`[data-cy="codeEtu"]`).should('have.value', 'ouch novice');

      cy.get(`[data-cy="ine"]`).type('cadre pour que');
      cy.get(`[data-cy="ine"]`).should('have.value', 'cadre pour que');

      cy.get(`[data-cy="codeBU"]`).type('pis deçà engloutir');
      cy.get(`[data-cy="codeBU"]`).should('have.value', 'pis deçà engloutir');

      cy.get(`[data-cy="emailUGB"]`).type('à la merci vouh');
      cy.get(`[data-cy="emailUGB"]`).should('have.value', 'à la merci vouh');

      cy.get(`[data-cy="dateNaissEtu"]`).type('2024-06-24');
      cy.get(`[data-cy="dateNaissEtu"]`).blur();
      cy.get(`[data-cy="dateNaissEtu"]`).should('have.value', '2024-06-24');

      cy.get(`[data-cy="lieuNaissEtu"]`).type('extrêmement');
      cy.get(`[data-cy="lieuNaissEtu"]`).should('have.value', 'extrêmement');

      cy.get(`[data-cy="sexe"]`).type('euh');
      cy.get(`[data-cy="sexe"]`).should('have.value', 'euh');

      cy.get(`[data-cy="numDocIdentite"]`).type('toc');
      cy.get(`[data-cy="numDocIdentite"]`).should('have.value', 'toc');

      cy.get(`[data-cy="assimileYN"]`).should('not.be.checked');
      cy.get(`[data-cy="assimileYN"]`).click();
      cy.get(`[data-cy="assimileYN"]`).should('be.checked');

      cy.get(`[data-cy="actifYN"]`).should('not.be.checked');
      cy.get(`[data-cy="actifYN"]`).click();
      cy.get(`[data-cy="actifYN"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        etudiant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', etudiantPageUrlPattern);
    });
  });
});
