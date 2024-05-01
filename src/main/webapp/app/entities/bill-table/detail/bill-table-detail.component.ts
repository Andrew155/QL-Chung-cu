import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IBillTable } from '../bill-table.model';

@Component({
  standalone: true,
  selector: 'jhi-bill-table-detail',
  templateUrl: './bill-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class BillTableDetailComponent {
  @Input() billTable: IBillTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
