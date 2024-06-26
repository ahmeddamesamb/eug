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

describe('TypeAdmission e2e test', () => {
  const typeAdmissionPageUrl = '/microservicegir/type-admission';
  const typeAdmissionPageUrlPattern = new RegExp('/microservicegir/type-admission(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const typeAdmissionSample = { libelleTypeAdmission: 'présidence au cas où' };

  let typeAdmission;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/services/microservicegir/api/type-admissions+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/services/microservicegir/api/type-admissions').as('postEntityRequest');
    cy.intercept('DELETE', '/services/microservicegir/api/type-admissions/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (typeAdmission) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/services/microservicegir/api/type-admissions/${typeAdmission.id}`,
      }).then(() => {
        typeAdmission = undefined;
      });
    }
  });

  it('TypeAdmissions menu should load TypeAdmissions page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('microservicegir/type-admission');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('TypeAdmission').should('exist');
    cy.url().should('match', typeAdmissionPageUrlPattern);
  });

  describe('TypeAdmission page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(typeAdmissionPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create TypeAdmission page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/microservicegir/type-admission/new$'));
        cy.getEntityCreateUpdateHeading('TypeAdmission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeAdmissionPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/services/microservicegir/api/type-admissions',
          body: typeAdmissionSample,
        }).then(({ body }) => {
          typeAdmission = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/services/microservicegir/api/type-admissions+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/services/microservicegir/api/type-admissions?page=0&size=20>; rel="last",<http://localhost/services/microservicegir/api/type-admissions?page=0&size=20>; rel="first"',
              },
              body: [typeAdmission],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(typeAdmissionPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details TypeAdmission page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('typeAdmission');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeAdmissionPageUrlPattern);
      });

      it('edit button click should load edit TypeAdmission page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeAdmission');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeAdmissionPageUrlPattern);
      });

      it('edit button click should load edit TypeAdmission page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('TypeAdmission');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeAdmissionPageUrlPattern);
      });

      it('last delete button click should delete instance of TypeAdmission', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('typeAdmission').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', typeAdmissionPageUrlPattern);

        typeAdmission = undefined;
      });
    });
  });

  describe('new TypeAdmission page', () => {
    beforeEach(() => {
      cy.visit(`${typeAdmissionPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('TypeAdmission');
    });

    it('should create an instance of TypeAdmission', () => {
      cy.get(`[data-cy="libelleTypeAdmission"]`).type('trop peu redouter');
      cy.get(`[data-cy="libelleTypeAdmission"]`).should('have.value', 'trop peu redouter');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        typeAdmission = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', typeAdmissionPageUrlPattern);
    });
  });
});
