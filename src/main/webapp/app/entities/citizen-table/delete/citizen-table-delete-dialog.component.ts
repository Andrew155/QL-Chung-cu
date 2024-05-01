import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICitizenTable } from '../citizen-table.model';
import { CitizenTableService } from '../service/citizen-table.service';

@Component({
  standalone: true,
  templateUrl: './citizen-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CitizenTableDeleteDialogComponent {
  citizenTable?: ICitizenTable;

  protected citizenTableService = inject(CitizenTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.citizenTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
