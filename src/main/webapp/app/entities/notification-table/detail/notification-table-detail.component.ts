import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { INotificationTable } from '../notification-table.model';

@Component({
  standalone: true,
  selector: 'jhi-notification-table-detail',
  templateUrl: './notification-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class NotificationTableDetailComponent {
  @Input() notificationTable: INotificationTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
