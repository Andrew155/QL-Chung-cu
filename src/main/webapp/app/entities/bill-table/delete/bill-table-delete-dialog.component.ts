import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IBillTable } from '../bill-table.model';
import { BillTableService } from '../service/bill-table.service';

@Component({
  standalone: true,
  templateUrl: './bill-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class BillTableDeleteDialogComponent {
  billTable?: IBillTable;

  protected billTableService = inject(BillTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.billTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
