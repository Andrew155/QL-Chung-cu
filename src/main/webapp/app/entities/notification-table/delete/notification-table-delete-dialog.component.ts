import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { INotificationTable } from '../notification-table.model';
import { NotificationTableService } from '../service/notification-table.service';

@Component({
  standalone: true,
  templateUrl: './notification-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class NotificationTableDeleteDialogComponent {
  notificationTable?: INotificationTable;

  protected notificationTableService = inject(NotificationTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.notificationTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
