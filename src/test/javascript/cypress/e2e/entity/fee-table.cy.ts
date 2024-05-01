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

describe('FeeTable e2e test', () => {
  const feeTablePageUrl = '/fee-table';
  const feeTablePageUrlPattern = new RegExp('/fee-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const feeTableSample = { feeId: 'for' };

  let feeTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/fee-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/fee-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/fee-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (feeTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/fee-tables/${feeTable.id}`,
      }).then(() => {
        feeTable = undefined;
      });
    }
  });

  it('FeeTables menu should load FeeTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('fee-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FeeTable').should('exist');
    cy.url().should('match', feeTablePageUrlPattern);
  });

  describe('FeeTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(feeTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FeeTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/fee-table/new$'));
        cy.getEntityCreateUpdateHeading('FeeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feeTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/fee-tables',
          body: feeTableSample,
        }).then(({ body }) => {
          feeTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/fee-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/fee-tables?page=0&size=20>; rel="last",<http://localhost/api/fee-tables?page=0&size=20>; rel="first"',
              },
              body: [feeTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(feeTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FeeTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('feeTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feeTablePageUrlPattern);
      });

      it('edit button click should load edit FeeTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeeTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feeTablePageUrlPattern);
      });

      it('edit button click should load edit FeeTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FeeTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feeTablePageUrlPattern);
      });

      it('last delete button click should delete instance of FeeTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('feeTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', feeTablePageUrlPattern);

        feeTable = undefined;
      });
    });
  });

  describe('new FeeTable page', () => {
    beforeEach(() => {
      cy.visit(`${feeTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FeeTable');
    });

    it('should create an instance of FeeTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-21T02:19');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-21T02:19');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T18:30');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T18:30');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T07:42');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T07:42');

      cy.get(`[data-cy="feeType"]`).type('meddle anenst whereas');
      cy.get(`[data-cy="feeType"]`).should('have.value', 'meddle anenst whereas');

      cy.get(`[data-cy="feeDesc"]`).type('forenenst meanwhile barge');
      cy.get(`[data-cy="feeDesc"]`).should('have.value', 'forenenst meanwhile barge');

      cy.get(`[data-cy="feeMonth"]`).type('cesspool fleece');
      cy.get(`[data-cy="feeMonth"]`).should('have.value', 'cesspool fleece');

      cy.get(`[data-cy="feeCost"]`).type('30728');
      cy.get(`[data-cy="feeCost"]`).should('have.value', '30728');

      cy.get(`[data-cy="date"]`).type('2024-04-20T11:17');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-04-20T11:17');

      cy.get(`[data-cy="status"]`).type('round primary plus');
      cy.get(`[data-cy="status"]`).should('have.value', 'round primary plus');

      cy.get(`[data-cy="feeId"]`).type('tinkling');
      cy.get(`[data-cy="feeId"]`).should('have.value', 'tinkling');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        feeTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', feeTablePageUrlPattern);
    });
  });
});
