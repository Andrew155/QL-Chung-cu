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

describe('NotificationTable e2e test', () => {
  const notificationTablePageUrl = '/notification-table';
  const notificationTablePageUrlPattern = new RegExp('/notification-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const notificationTableSample = {};

  let notificationTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/notification-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/notification-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/notification-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (notificationTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/notification-tables/${notificationTable.id}`,
      }).then(() => {
        notificationTable = undefined;
      });
    }
  });

  it('NotificationTables menu should load NotificationTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('notification-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('NotificationTable').should('exist');
    cy.url().should('match', notificationTablePageUrlPattern);
  });

  describe('NotificationTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(notificationTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create NotificationTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/notification-table/new$'));
        cy.getEntityCreateUpdateHeading('NotificationTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/notification-tables',
          body: notificationTableSample,
        }).then(({ body }) => {
          notificationTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/notification-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [notificationTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(notificationTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details NotificationTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('notificationTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationTablePageUrlPattern);
      });

      it('edit button click should load edit NotificationTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotificationTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationTablePageUrlPattern);
      });

      it('edit button click should load edit NotificationTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('NotificationTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationTablePageUrlPattern);
      });

      it('last delete button click should delete instance of NotificationTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('notificationTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', notificationTablePageUrlPattern);

        notificationTable = undefined;
      });
    });
  });

  describe('new NotificationTable page', () => {
    beforeEach(() => {
      cy.visit(`${notificationTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('NotificationTable');
    });

    it('should create an instance of NotificationTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T17:46');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T17:46');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T16:36');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T16:36');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T08:54');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T08:54');

      cy.get(`[data-cy="title"]`).type('hearty');
      cy.get(`[data-cy="title"]`).should('have.value', 'hearty');

      cy.get(`[data-cy="content"]`).type('our');
      cy.get(`[data-cy="content"]`).should('have.value', 'our');

      cy.get(`[data-cy="userID"]`).type('fluid after excitedly');
      cy.get(`[data-cy="userID"]`).should('have.value', 'fluid after excitedly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        notificationTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', notificationTablePageUrlPattern);
    });
  });
});
