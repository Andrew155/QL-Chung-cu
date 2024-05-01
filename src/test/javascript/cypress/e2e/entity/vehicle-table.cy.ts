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

describe('VehicleTable e2e test', () => {
  const vehicleTablePageUrl = '/vehicle-table';
  const vehicleTablePageUrlPattern = new RegExp('/vehicle-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const vehicleTableSample = { vehicleId: 'furthermore considering deficit' };

  let vehicleTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/vehicle-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/vehicle-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/vehicle-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (vehicleTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/vehicle-tables/${vehicleTable.id}`,
      }).then(() => {
        vehicleTable = undefined;
      });
    }
  });

  it('VehicleTables menu should load VehicleTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('vehicle-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('VehicleTable').should('exist');
    cy.url().should('match', vehicleTablePageUrlPattern);
  });

  describe('VehicleTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(vehicleTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create VehicleTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/vehicle-table/new$'));
        cy.getEntityCreateUpdateHeading('VehicleTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/vehicle-tables',
          body: vehicleTableSample,
        }).then(({ body }) => {
          vehicleTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/vehicle-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/vehicle-tables?page=0&size=20>; rel="last",<http://localhost/api/vehicle-tables?page=0&size=20>; rel="first"',
              },
              body: [vehicleTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(vehicleTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details VehicleTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('vehicleTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTablePageUrlPattern);
      });

      it('edit button click should load edit VehicleTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTablePageUrlPattern);
      });

      it('edit button click should load edit VehicleTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('VehicleTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTablePageUrlPattern);
      });

      it('last delete button click should delete instance of VehicleTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('vehicleTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', vehicleTablePageUrlPattern);

        vehicleTable = undefined;
      });
    });
  });

  describe('new VehicleTable page', () => {
    beforeEach(() => {
      cy.visit(`${vehicleTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('VehicleTable');
    });

    it('should create an instance of VehicleTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T22:40');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T22:40');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T18:43');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T18:43');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T05:30');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T05:30');

      cy.get(`[data-cy="vehicleName"]`).type('that keenly storm');
      cy.get(`[data-cy="vehicleName"]`).should('have.value', 'that keenly storm');

      cy.get(`[data-cy="vehicleType"]`).type('colorfully decontrol until');
      cy.get(`[data-cy="vehicleType"]`).should('have.value', 'colorfully decontrol until');

      cy.get(`[data-cy="vehicleId"]`).type('silently');
      cy.get(`[data-cy="vehicleId"]`).should('have.value', 'silently');

      cy.get(`[data-cy="roomId"]`).type('premium duh provided');
      cy.get(`[data-cy="roomId"]`).should('have.value', 'premium duh provided');

      cy.get(`[data-cy="ownerId"]`).type('notwithstanding');
      cy.get(`[data-cy="ownerId"]`).should('have.value', 'notwithstanding');

      cy.get(`[data-cy="vehicleFee"]`).type('10261');
      cy.get(`[data-cy="vehicleFee"]`).should('have.value', '10261');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        vehicleTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', vehicleTablePageUrlPattern);
    });
  });
});
