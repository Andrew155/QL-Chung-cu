import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IVehicleTable } from '../vehicle-table.model';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-table-detail',
  templateUrl: './vehicle-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class VehicleTableDetailComponent {
  @Input() vehicleTable: IVehicleTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
