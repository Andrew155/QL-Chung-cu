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

describe('RoomTable e2e test', () => {
  const roomTablePageUrl = '/room-table';
  const roomTablePageUrlPattern = new RegExp('/room-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const roomTableSample = { roomId: 'even' };

  let roomTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/room-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/room-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/room-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (roomTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/room-tables/${roomTable.id}`,
      }).then(() => {
        roomTable = undefined;
      });
    }
  });

  it('RoomTables menu should load RoomTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('room-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('RoomTable').should('exist');
    cy.url().should('match', roomTablePageUrlPattern);
  });

  describe('RoomTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(roomTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create RoomTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/room-table/new$'));
        cy.getEntityCreateUpdateHeading('RoomTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roomTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/room-tables',
          body: roomTableSample,
        }).then(({ body }) => {
          roomTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/room-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/room-tables?page=0&size=20>; rel="last",<http://localhost/api/room-tables?page=0&size=20>; rel="first"',
              },
              body: [roomTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(roomTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details RoomTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('roomTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roomTablePageUrlPattern);
      });

      it('edit button click should load edit RoomTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoomTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roomTablePageUrlPattern);
      });

      it('edit button click should load edit RoomTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('RoomTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roomTablePageUrlPattern);
      });

      it('last delete button click should delete instance of RoomTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('roomTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', roomTablePageUrlPattern);

        roomTable = undefined;
      });
    });
  });

  describe('new RoomTable page', () => {
    beforeEach(() => {
      cy.visit(`${roomTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('RoomTable');
    });

    it('should create an instance of RoomTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T22:48');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T22:48');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T19:47');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T19:47');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T16:44');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T16:44');

      cy.get(`[data-cy="roomId"]`).type('yawning');
      cy.get(`[data-cy="roomId"]`).should('have.value', 'yawning');

      cy.get(`[data-cy="area"]`).type('reject ha');
      cy.get(`[data-cy="area"]`).should('have.value', 'reject ha');

      cy.get(`[data-cy="ownTime"]`).type('winged whether');
      cy.get(`[data-cy="ownTime"]`).should('have.value', 'winged whether');

      cy.get(`[data-cy="ownerId"]`).type('slosh for indeed');
      cy.get(`[data-cy="ownerId"]`).should('have.value', 'slosh for indeed');

      cy.get(`[data-cy="ownerName"]`).type('nurture monthly mostly');
      cy.get(`[data-cy="ownerName"]`).should('have.value', 'nurture monthly mostly');

      cy.get(`[data-cy="status"]`).type('though geez');
      cy.get(`[data-cy="status"]`).should('have.value', 'though geez');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        roomTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', roomTablePageUrlPattern);
    });
  });
});
