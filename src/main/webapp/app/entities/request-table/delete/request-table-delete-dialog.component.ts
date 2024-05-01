import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRequestTable } from '../request-table.model';
import { RequestTableService } from '../service/request-table.service';

@Component({
  standalone: true,
  templateUrl: './request-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RequestTableDeleteDialogComponent {
  requestTable?: IRequestTable;

  protected requestTableService = inject(RequestTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.requestTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
