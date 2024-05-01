import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDonationTable } from '../donation-table.model';
import { DonationTableService } from '../service/donation-table.service';

@Component({
  standalone: true,
  templateUrl: './donation-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DonationTableDeleteDialogComponent {
  donationTable?: IDonationTable;

  protected donationTableService = inject(DonationTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donationTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
