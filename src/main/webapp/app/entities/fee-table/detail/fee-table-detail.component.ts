import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IFeeTable } from '../fee-table.model';

@Component({
  standalone: true,
  selector: 'jhi-fee-table-detail',
  templateUrl: './fee-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class FeeTableDetailComponent {
  @Input() feeTable: IFeeTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
