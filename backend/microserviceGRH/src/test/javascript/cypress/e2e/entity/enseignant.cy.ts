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

describe('Enseignant e2e test', () => {
  const enseignantPageUrl = '/microservicegrh/enseignant';
  const enseignantPageUrlPattern = new RegExp('/microservicegrh/enseignant(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const enseignantSample = { nom: 'mélancolique régler', prenom: 'rapprocher jusqu’à ce que' };

  let enseignant;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegrh/api/enseignants+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegrh/api/enseignants').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegrh/api/enseignants/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (enseignant) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegrh/api/enseignants/${enseignant.id}`,
      }).then(() => {
        enseignant = undefined;
      });
    }
  });

  it('Enseignants menu should load Enseignants page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegrh/enseignant');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Enseignant').should('exist');
    cy.url().should('match', enseignantPageUrlPattern);
  });

  describe('Enseignant page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(enseignantPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Enseignant page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegrh/enseignant/new$'));
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegrh/api/enseignants',
          body: enseignantSample,
        }).then(({ body }) => {
          enseignant = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegrh/api/enseignants+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegrh/api/enseignants?page=0&size=20>; rel="last",<http://localhost/services/microservicegrh/api/enseignants?page=0&size=20>; rel="first"',
              },
              body: [enseignant],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(enseignantPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Enseignant page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('enseignant');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('edit button click should load edit Enseignant page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('edit button click should load edit Enseignant page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Enseignant');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);
      });

      it('last delete button click should delete instance of Enseignant', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('enseignant').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', enseignantPageUrlPattern);

        enseignant = undefined;
      });
    });
  });

  describe('new Enseignant page', () => {
    beforeEach(() => {
      cy.visit(`${enseignantPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Enseignant');
    });

    it('should create an instance of Enseignant', () => {
      cy.get(`[data-cy="titreCoEncadreur"]`).type('clientèle commissionnaire tandis que');
      cy.get(`[data-cy="titreCoEncadreur"]`).should('have.value', 'clientèle commissionnaire tandis que');

      cy.get(`[data-cy="nom"]`).type('plouf');
      cy.get(`[data-cy="nom"]`).should('have.value', 'plouf');

      cy.get(`[data-cy="prenom"]`).type('à condition que sous porte-parole');
      cy.get(`[data-cy="prenom"]`).should('have.value', 'à condition que sous porte-parole');

      cy.get(`[data-cy="email"]`).type('Fanny_Renault@hotmail.fr');
      cy.get(`[data-cy="email"]`).should('have.value', 'Fanny_Renault@hotmail.fr');

      cy.get(`[data-cy="telephone"]`).type('+33 120193196');
      cy.get(`[data-cy="telephone"]`).should('have.value', '+33 120193196');

      cy.get(`[data-cy="titresId"]`).type('11449');
      cy.get(`[data-cy="titresId"]`).should('have.value', '11449');

      cy.get(`[data-cy="adresse"]`).type('afin que simple');
      cy.get(`[data-cy="adresse"]`).should('have.value', 'afin que simple');

      cy.get(`[data-cy="numeroPoste"]`).type('3203');
      cy.get(`[data-cy="numeroPoste"]`).should('have.value', '3203');

      cy.get(`[data-cy="photo"]`).type('de façon à');
      cy.get(`[data-cy="photo"]`).should('have.value', 'de façon à');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        enseignant = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', enseignantPageUrlPattern);
    });
  });
});
