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

describe('CitizenTable e2e test', () => {
  const citizenTablePageUrl = '/citizen-table';
  const citizenTablePageUrlPattern = new RegExp('/citizen-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const citizenTableSample = { citizenID: 'whenever interleave whoa' };

  let citizenTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/citizen-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/citizen-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/citizen-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (citizenTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/citizen-tables/${citizenTable.id}`,
      }).then(() => {
        citizenTable = undefined;
      });
    }
  });

  it('CitizenTables menu should load CitizenTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('citizen-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CitizenTable').should('exist');
    cy.url().should('match', citizenTablePageUrlPattern);
  });

  describe('CitizenTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(citizenTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CitizenTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/citizen-table/new$'));
        cy.getEntityCreateUpdateHeading('CitizenTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', citizenTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/citizen-tables',
          body: citizenTableSample,
        }).then(({ body }) => {
          citizenTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/citizen-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [citizenTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(citizenTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CitizenTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('citizenTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', citizenTablePageUrlPattern);
      });

      it('edit button click should load edit CitizenTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CitizenTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', citizenTablePageUrlPattern);
      });

      it('edit button click should load edit CitizenTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CitizenTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', citizenTablePageUrlPattern);
      });

      it('last delete button click should delete instance of CitizenTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('citizenTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', citizenTablePageUrlPattern);

        citizenTable = undefined;
      });
    });
  });

  describe('new CitizenTable page', () => {
    beforeEach(() => {
      cy.visit(`${citizenTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CitizenTable');
    });

    it('should create an instance of CitizenTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T08:42');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T08:42');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-20T13:44');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-20T13:44');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T17:35');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T17:35');

      cy.get(`[data-cy="citizenID"]`).type('temple wherever pace');
      cy.get(`[data-cy="citizenID"]`).should('have.value', 'temple wherever pace');

      cy.get(`[data-cy="name"]`).type('given');
      cy.get(`[data-cy="name"]`).should('have.value', 'given');

      cy.get(`[data-cy="dob"]`).type('seagull');
      cy.get(`[data-cy="dob"]`).should('have.value', 'seagull');

      cy.get(`[data-cy="contact"]`).type('pro oof ha');
      cy.get(`[data-cy="contact"]`).should('have.value', 'pro oof ha');

      cy.get(`[data-cy="gender"]`).type('tall');
      cy.get(`[data-cy="gender"]`).should('have.value', 'tall');

      cy.get(`[data-cy="relation"]`).type('curl psychoanalyse');
      cy.get(`[data-cy="relation"]`).should('have.value', 'curl psychoanalyse');

      cy.get(`[data-cy="status"]`).type('fussy crossly');
      cy.get(`[data-cy="status"]`).should('have.value', 'fussy crossly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        citizenTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', citizenTablePageUrlPattern);
    });
  });
});
