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

describe('InformationPersonnelle e2e test', () => {
  const informationPersonnellePageUrl = '/microservicegir/information-personnelle';
  const informationPersonnellePageUrlPattern = new RegExp('/microservicegir/information-personnelle(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const informationPersonnelleSample = {
    nomEtu: 'mairie',
    prenomEtu: 'désagréable',
    statutMarital: 'snif communauté étudiante de peur que',
    adresseEtu: 'quoique',
  };

  let informationPersonnelle;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/information-personnelles+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/information-personnelles').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/information-personnelles/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (informationPersonnelle) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/information-personnelles/${informationPersonnelle.id}`,
      }).then(() => {
        informationPersonnelle = undefined;
      });
    }
  });

  it('InformationPersonnelles menu should load InformationPersonnelles page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/information-personnelle');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InformationPersonnelle').should('exist');
    cy.url().should('match', informationPersonnellePageUrlPattern);
  });

  describe('InformationPersonnelle page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(informationPersonnellePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InformationPersonnelle page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/information-personnelle/new$'));
        cy.getEntityCreateUpdateHeading('InformationPersonnelle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPersonnellePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/information-personnelles',
          body: informationPersonnelleSample,
        }).then(({ body }) => {
          informationPersonnelle = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/information-personnelles+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/information-personnelles?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/information-personnelles?page=0&size=20>; rel="first"',
              },
              body: [informationPersonnelle],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(informationPersonnellePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InformationPersonnelle page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('informationPersonnelle');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPersonnellePageUrlPattern);
      });

      it('edit button click should load edit InformationPersonnelle page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InformationPersonnelle');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPersonnellePageUrlPattern);
      });

      it('edit button click should load edit InformationPersonnelle page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InformationPersonnelle');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPersonnellePageUrlPattern);
      });

      it('last delete button click should delete instance of InformationPersonnelle', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('informationPersonnelle').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationPersonnellePageUrlPattern);

        informationPersonnelle = undefined;
      });
    });
  });

  describe('new InformationPersonnelle page', () => {
    beforeEach(() => {
      cy.visit(`${informationPersonnellePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InformationPersonnelle');
    });

    it('should create an instance of InformationPersonnelle', () => {
      cy.get(`[data-cy="nomEtu"]`).type('dans chef');
      cy.get(`[data-cy="nomEtu"]`).should('have.value', 'dans chef');

      cy.get(`[data-cy="nomJeuneFilleEtu"]`).type('immense');
      cy.get(`[data-cy="nomJeuneFilleEtu"]`).should('have.value', 'immense');

      cy.get(`[data-cy="prenomEtu"]`).type('snif après que');
      cy.get(`[data-cy="prenomEtu"]`).should('have.value', 'snif après que');

      cy.get(`[data-cy="statutMarital"]`).type('divinement');
      cy.get(`[data-cy="statutMarital"]`).should('have.value', 'divinement');

      cy.get(`[data-cy="regime"]`).type('4901');
      cy.get(`[data-cy="regime"]`).should('have.value', '4901');

      cy.get(`[data-cy="profession"]`).type('suivant rectangulaire');
      cy.get(`[data-cy="profession"]`).should('have.value', 'suivant rectangulaire');

      cy.get(`[data-cy="adresseEtu"]`).type('quant à progresser');
      cy.get(`[data-cy="adresseEtu"]`).should('have.value', 'quant à progresser');

      cy.get(`[data-cy="telEtu"]`).type('loin de');
      cy.get(`[data-cy="telEtu"]`).should('have.value', 'loin de');

      cy.get(`[data-cy="emailEtu"]`).type("à l'exception de proclamer");
      cy.get(`[data-cy="emailEtu"]`).should('have.value', "à l'exception de proclamer");

      cy.get(`[data-cy="adresseParent"]`).type('lâche');
      cy.get(`[data-cy="adresseParent"]`).should('have.value', 'lâche');

      cy.get(`[data-cy="telParent"]`).type('boum');
      cy.get(`[data-cy="telParent"]`).should('have.value', 'boum');

      cy.get(`[data-cy="emailParent"]`).type('rapide dépenser dynamique');
      cy.get(`[data-cy="emailParent"]`).should('have.value', 'rapide dépenser dynamique');

      cy.get(`[data-cy="nomParent"]`).type('bè entre recta');
      cy.get(`[data-cy="nomParent"]`).should('have.value', 'bè entre recta');

      cy.get(`[data-cy="prenomParent"]`).type('cocorico moderne');
      cy.get(`[data-cy="prenomParent"]`).should('have.value', 'cocorico moderne');

      cy.get(`[data-cy="handicapYN"]`).should('not.be.checked');
      cy.get(`[data-cy="handicapYN"]`).click();
      cy.get(`[data-cy="handicapYN"]`).should('be.checked');

      cy.get(`[data-cy="photo"]`).type('au point que');
      cy.get(`[data-cy="photo"]`).should('have.value', 'au point que');

      cy.get(`[data-cy="ordiPersoYN"]`).should('not.be.checked');
      cy.get(`[data-cy="ordiPersoYN"]`).click();
      cy.get(`[data-cy="ordiPersoYN"]`).should('be.checked');

      cy.get(`[data-cy="derniereModification"]`).type('2024-06-25');
      cy.get(`[data-cy="derniereModification"]`).blur();
      cy.get(`[data-cy="derniereModification"]`).should('have.value', '2024-06-25');

      cy.get(`[data-cy="emailUser"]`).type('circulaire');
      cy.get(`[data-cy="emailUser"]`).should('have.value', 'circulaire');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        informationPersonnelle = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', informationPersonnellePageUrlPattern);
    });
  });
});
