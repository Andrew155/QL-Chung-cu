import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicleTable } from '../vehicle-table.model';
import { VehicleTableService } from '../service/vehicle-table.service';

@Component({
  standalone: true,
  templateUrl: './vehicle-table-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehicleTableDeleteDialogComponent {
  vehicleTable?: IVehicleTable;

  protected vehicleTableService = inject(VehicleTableService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleTableService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
