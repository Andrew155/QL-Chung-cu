import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IRoomTable } from '../room-table.model';
import { RoomTableService } from '../service/room-table.service';

@Component({
  standalone: true,
  templateUrl: './room-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class RoomTableDeleteDialogComponent {
  roomTable?: IRoomTable;

  protected roomTableService = inject(RoomTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
