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

describe('TypeBourse e2e test', () => {
  const typeBoursePageUrl = '/microservicegir/type-bourse';
  const typeBoursePageUrlPattern = new RegExp('/microservicegir/type-bourse(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeBourseSample = { libelleTypeBourse: 'toutefois en' };

  let typeBourse;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-bourses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-bourses').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-bourses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeBourse) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-bourses/${typeBourse.id}`,
      }).then(() => {
        typeBourse = undefined;
      });
    }
  });

  it('TypeBourses menu should load TypeBourses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-bourse');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeBourse').should('exist');
    cy.url().should('match', typeBoursePageUrlPattern);
  });

  describe('TypeBourse page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeBoursePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeBourse page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-bourse/new$'));
        cy.getEntityCreateUpdateHeading('TypeBourse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeBoursePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-bourses',
          body: typeBourseSample,
        }).then(({ body }) => {
          typeBourse = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-bourses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-bourses?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-bourses?page=0&size=20>; rel="first"',
              },
              body: [typeBourse],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeBoursePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeBourse page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeBourse');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeBoursePageUrlPattern);
      });

      it('edit button click should load edit TypeBourse page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeBourse');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeBoursePageUrlPattern);
      });

      it('edit button click should load edit TypeBourse page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeBourse');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeBoursePageUrlPattern);
      });

      it('last delete button click should delete instance of TypeBourse', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeBourse').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeBoursePageUrlPattern);

        typeBourse = undefined;
      });
    });
  });

  describe('new TypeBourse page', () => {
    beforeEach(() => {
      cy.visit(`${typeBoursePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeBourse');
    });

    it('should create an instance of TypeBourse', () => {
      cy.get(`[data-cy="libelleTypeBourse"]`).type('prout communauté étudiante');
      cy.get(`[data-cy="libelleTypeBourse"]`).should('have.value', 'prout communauté étudiante');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeBourse = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeBoursePageUrlPattern);
    });
  });
});
