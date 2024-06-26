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

describe('InformationImage e2e test', () => {
  const informationImagePageUrl = '/microservicegir/information-image';
  const informationImagePageUrlPattern = new RegExp('/microservicegir/information-image(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const informationImageSample = { cheminPath: 'quelque totalement évacuer', cheminFile: 'mature dès que' };

  let informationImage;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/information-images+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/information-images').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/information-images/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (informationImage) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/information-images/${informationImage.id}`,
      }).then(() => {
        informationImage = undefined;
      });
    }
  });

  it('InformationImages menu should load InformationImages page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/information-image');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InformationImage').should('exist');
    cy.url().should('match', informationImagePageUrlPattern);
  });

  describe('InformationImage page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(informationImagePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InformationImage page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/information-image/new$'));
        cy.getEntityCreateUpdateHeading('InformationImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationImagePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/information-images',
          body: informationImageSample,
        }).then(({ body }) => {
          informationImage = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/information-images+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/information-images?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/information-images?page=0&size=20>; rel="first"',
              },
              body: [informationImage],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(informationImagePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InformationImage page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('informationImage');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationImagePageUrlPattern);
      });

      it('edit button click should load edit InformationImage page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InformationImage');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationImagePageUrlPattern);
      });

      it('edit button click should load edit InformationImage page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InformationImage');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationImagePageUrlPattern);
      });

      it('last delete button click should delete instance of InformationImage', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('informationImage').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', informationImagePageUrlPattern);

        informationImage = undefined;
      });
    });
  });

  describe('new InformationImage page', () => {
    beforeEach(() => {
      cy.visit(`${informationImagePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InformationImage');
    });

    it('should create an instance of InformationImage', () => {
      cy.get(`[data-cy="description"]`).type('pour');
      cy.get(`[data-cy="description"]`).should('have.value', 'pour');

      cy.get(`[data-cy="cheminPath"]`).type('ouin');
      cy.get(`[data-cy="cheminPath"]`).should('have.value', 'ouin');

      cy.get(`[data-cy="cheminFile"]`).type('avant de virer');
      cy.get(`[data-cy="cheminFile"]`).should('have.value', 'avant de virer');

      cy.get(`[data-cy="enCoursYN"]`).type('de manière à candide');
      cy.get(`[data-cy="enCoursYN"]`).should('have.value', 'de manière à candide');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        informationImage = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', informationImagePageUrlPattern);
    });
  });
});
