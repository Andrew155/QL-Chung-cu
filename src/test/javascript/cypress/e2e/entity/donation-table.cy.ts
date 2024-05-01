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

describe('DonationTable e2e test', () => {
  const donationTablePageUrl = '/donation-table';
  const donationTablePageUrlPattern = new RegExp('/donation-table(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const donationTableSample = { donationId: 'hmph nag' };

  let donationTable;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/donation-tables+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/donation-tables').as('postEntityRequest');
    cy.intercept('DELETE', '/api/donation-tables/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (donationTable) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/donation-tables/${donationTable.id}`,
      }).then(() => {
        donationTable = undefined;
      });
    }
  });

  it('DonationTables menu should load DonationTables page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('donation-table');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DonationTable').should('exist');
    cy.url().should('match', donationTablePageUrlPattern);
  });

  describe('DonationTable page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(donationTablePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DonationTable page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/donation-table/new$'));
        cy.getEntityCreateUpdateHeading('DonationTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTablePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/donation-tables',
          body: donationTableSample,
        }).then(({ body }) => {
          donationTable = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/donation-tables+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/donation-tables?page=0&size=20>; rel="last",<http://localhost/api/donation-tables?page=0&size=20>; rel="first"',
              },
              body: [donationTable],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(donationTablePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DonationTable page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('donationTable');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTablePageUrlPattern);
      });

      it('edit button click should load edit DonationTable page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DonationTable');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTablePageUrlPattern);
      });

      it('edit button click should load edit DonationTable page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DonationTable');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTablePageUrlPattern);
      });

      it('last delete button click should delete instance of DonationTable', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('donationTable').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', donationTablePageUrlPattern);

        donationTable = undefined;
      });
    });
  });

  describe('new DonationTable page', () => {
    beforeEach(() => {
      cy.visit(`${donationTablePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DonationTable');
    });

    it('should create an instance of DonationTable', () => {
      cy.get(`[data-cy="createAt"]`).type('2024-04-20T20:28');
      cy.get(`[data-cy="createAt"]`).blur();
      cy.get(`[data-cy="createAt"]`).should('have.value', '2024-04-20T20:28');

      cy.get(`[data-cy="updateAt"]`).type('2024-04-21T03:42');
      cy.get(`[data-cy="updateAt"]`).blur();
      cy.get(`[data-cy="updateAt"]`).should('have.value', '2024-04-21T03:42');

      cy.get(`[data-cy="deletedAt"]`).type('2024-04-20T13:32');
      cy.get(`[data-cy="deletedAt"]`).blur();
      cy.get(`[data-cy="deletedAt"]`).should('have.value', '2024-04-20T13:32');

      cy.get(`[data-cy="donationId"]`).type('comfortable easy');
      cy.get(`[data-cy="donationId"]`).should('have.value', 'comfortable easy');

      cy.get(`[data-cy="donationType"]`).type('apud');
      cy.get(`[data-cy="donationType"]`).should('have.value', 'apud');

      cy.get(`[data-cy="donationDesc"]`).type('alongside unwitting blah');
      cy.get(`[data-cy="donationDesc"]`).should('have.value', 'alongside unwitting blah');

      cy.get(`[data-cy="donationMonth"]`).type('fully close comfortable');
      cy.get(`[data-cy="donationMonth"]`).should('have.value', 'fully close comfortable');

      cy.get(`[data-cy="donationCost"]`).type('17896');
      cy.get(`[data-cy="donationCost"]`).should('have.value', '17896');

      cy.get(`[data-cy="roomId"]`).type('shack unless');
      cy.get(`[data-cy="roomId"]`).should('have.value', 'shack unless');

      cy.get(`[data-cy="status"]`).type('anchored rework');
      cy.get(`[data-cy="status"]`).should('have.value', 'anchored rework');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        donationTable = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', donationTablePageUrlPattern);
    });
  });
});
