import { Component, Input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICitizenTable } from '../citizen-table.model';

@Component({
  standalone: true,
  selector: 'jhi-citizen-table-detail',
  templateUrl: './citizen-table-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CitizenTableDetailComponent {
  @Input() citizenTable: ICitizenTable | null = null;

  previousState(): void {
    window.history.back();
  }
}
