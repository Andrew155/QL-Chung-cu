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

describe('BillTable e2e test', () => {
  const billTablePageUrl = '/bill-table';
  const billTablePageUrlPattern = new RegExp('/bill-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const billTableSample = { billId: 'shrilly' };

  let billTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/bill-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/bill-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/bill-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (billTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/bill-tables/${billTable.id}`,
      }).then(() => {
        billTable = undefined;
      });
    }
  });

  it('BillTables menu should load BillTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('bill-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('BillTable').should('exist');
    cy.url().should('match', billTablePageUrlPattern);
  });

  describe('BillTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(billTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create BillTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/bill-table/new$'));
        cy.getEntityCreateUpdateHeading('BillTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', billTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/bill-tables',
          body: billTableSample,
        }).then(({ body }) => {
          billTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/bill-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [billTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(billTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details BillTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('billTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', billTablePageUrlPattern);
      });

      it('edit button click should load edit BillTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BillTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', billTablePageUrlPattern);
      });

      it('edit button click should load edit BillTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('BillTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', billTablePageUrlPattern);
      });

      it('last delete button click should delete instance of BillTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('billTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', billTablePageUrlPattern);

        billTable = undefined;
      });
    });
  });

  describe('new BillTable page', () => {
    beforeEach(() => {
      cy.visit(`${billTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('BillTable');
    });

    it('should create an instance of BillTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T22:37');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T22:37');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-21T00:03');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-21T00:03');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T17:23');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T17:23');

      cy.get(`[data-cy="billType"]`).type('or');
      cy.get(`[data-cy="billType"]`).should('have.value', 'or');

      cy.get(`[data-cy="billId"]`).type('terrorise yowza');
      cy.get(`[data-cy="billId"]`).should('have.value', 'terrorise yowza');

      cy.get(`[data-cy="billMonth"]`).type('zampone following');
      cy.get(`[data-cy="billMonth"]`).should('have.value', 'zampone following');

      cy.get(`[data-cy="billAmount"]`).type('4397');
      cy.get(`[data-cy="billAmount"]`).should('have.value', '4397');

      cy.get(`[data-cy="roomId"]`).type('round');
      cy.get(`[data-cy="roomId"]`).should('have.value', 'round');

      cy.get(`[data-cy="date"]`).type('2024-04-20T15:56');
      cy.get(`[data-cy="date"]`).blur();
      cy.get(`[data-cy="date"]`).should('have.value', '2024-04-20T15:56');

      cy.get(`[data-cy="status"]`).type('prestigious gosh');
      cy.get(`[data-cy="status"]`).should('have.value', 'prestigious gosh');

      cy.get(`[data-cy="billCost"]`).type('28864');
      cy.get(`[data-cy="billCost"]`).should('have.value', '28864');

      cy.get(`[data-cy="customerID"]`).type('shameful innocently wholly');
      cy.get(`[data-cy="customerID"]`).should('have.value', 'shameful innocently wholly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        billTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', billTablePageUrlPattern);
    });
  });
});
