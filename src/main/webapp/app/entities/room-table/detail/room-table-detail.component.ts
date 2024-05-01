import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRoomTable } from '../room-table.model';

@Component({
  standalone: true,
  selector: 'jhi-room-table-detail',
  templateUrl: './room-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RoomTableDetailComponent {
  @Input() roomTable: IRoomTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
