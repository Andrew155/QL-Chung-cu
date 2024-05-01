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

describe('RequestTable e2e test', () => {
  const requestTablePageUrl = '/request-table';
  const requestTablePageUrlPattern = new RegExp('/request-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const requestTableSample = {};

  let requestTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/request-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/request-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/request-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (requestTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/request-tables/${requestTable.id}`,
      }).then(() => {
        requestTable = undefined;
      });
    }
  });

  it('RequestTables menu should load RequestTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('request-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RequestTable').should('exist');
    cy.url().should('match', requestTablePageUrlPattern);
  });

  describe('RequestTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(requestTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RequestTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/request-table/new$'));
        cy.getEntityCreateUpdateHeading('RequestTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', requestTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/request-tables',
          body: requestTableSample,
        }).then(({ body }) => {
          requestTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/request-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [requestTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(requestTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RequestTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('requestTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', requestTablePageUrlPattern);
      });

      it('edit button click should load edit RequestTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RequestTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', requestTablePageUrlPattern);
      });

      it('edit button click should load edit RequestTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RequestTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', requestTablePageUrlPattern);
      });

      it('last delete button click should delete instance of RequestTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('requestTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', requestTablePageUrlPattern);

        requestTable = undefined;
      });
    });
  });

  describe('new RequestTable page', () => {
    beforeEach(() => {
      cy.visit(`${requestTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RequestTable');
    });

    it('should create an instance of RequestTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T18:50');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T18:50');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T15:53');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T15:53');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T09:31');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T09:31');

      cy.get(`[data-cy="status"]`).type('sing');
      cy.get(`[data-cy="status"]`).should('have.value', 'sing');

      cy.get(`[data-cy="userId"]`).type('wool yum gesticulate');
      cy.get(`[data-cy="userId"]`).should('have.value', 'wool yum gesticulate');

      cy.get(`[data-cy="title"]`).type('gah inasmuch');
      cy.get(`[data-cy="title"]`).should('have.value', 'gah inasmuch');

      cy.get(`[data-cy="message"]`).type('vaguely duh');
      cy.get(`[data-cy="message"]`).should('have.value', 'vaguely duh');

      cy.get(`[data-cy="reply"]`).type('hmph than tragic');
      cy.get(`[data-cy="reply"]`).should('have.value', 'hmph than tragic');

      cy.get(`[data-cy="note"]`).type('ringworm');
      cy.get(`[data-cy="note"]`).should('have.value', 'ringworm');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        requestTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', requestTablePageUrlPattern);
    });
  });
});
