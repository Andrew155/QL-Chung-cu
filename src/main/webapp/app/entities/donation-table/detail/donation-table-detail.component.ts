import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDonationTable } from '../donation-table.model';

@Component({
  standalone: true,
  selector: 'jhi-donation-table-detail',
  templateUrl: './donation-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DonationTableDetailComponent {
  @Input() donationTable: IDonationTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
