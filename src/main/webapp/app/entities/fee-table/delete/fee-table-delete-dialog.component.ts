import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IFeeTable } from '../fee-table.model';
import { FeeTableService } from '../service/fee-table.service';

@Component({
  standalone: true,
  templateUrl: './fee-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FeeTableDeleteDialogComponent {
  feeTable?: IFeeTable;

  protected feeTableService = inject(FeeTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.feeTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
