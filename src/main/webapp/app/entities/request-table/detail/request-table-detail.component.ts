import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IRequestTable } from '../request-table.model';

@Component({
  standalone: true,
  selector: 'jhi-request-table-detail',
  templateUrl: './request-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class RequestTableDetailComponent {
  @Input() requestTable: IRequestTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
