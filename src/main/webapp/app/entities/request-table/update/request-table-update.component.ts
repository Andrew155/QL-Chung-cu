import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { INotificationTable } from 'app/entities/notification-table/notification-table.model';
import { NotificationTableService } from 'app/entities/notification-table/service/notification-table.service';
import { IRequestTable } from '../request-table.model';
import { RequestTableService } from '../service/request-table.service';
import { RequestTableFormService, RequestTableFormGroup } from './request-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-request-table-update',
  templateUrl: './request-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RequestTableUpdateComponent implements OnInit {
  isSaving = false;
  requestTable: IRequestTable | null = null;

  notificationTablesCollection: INotificationTable[] = [];

  protected requestTableService = inject(RequestTableService);
  protected requestTableFormService = inject(RequestTableFormService);
  protected notificationTableService = inject(NotificationTableService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RequestTableFormGroup = this.requestTableFormService.createRequestTableFormGroup();

  compareNotificationTable = (o1: INotificationTable | null, o2: INotificationTable | null): boolean =>
    this.notificationTableService.compareNotificationTable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ requestTable }) => {
      this.requestTable = requestTable;
      if (requestTable) {
        this.updateForm(requestTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const requestTable = this.requestTableFormService.getRequestTable(this.editForm);
    if (requestTable.id !== null) {
      this.subscribeToSaveResponse(this.requestTableService.update(requestTable));
    } else {
      this.subscribeToSaveResponse(this.requestTableService.create(requestTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRequestTable>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(requestTable: IRequestTable): void {
    this.requestTable = requestTable;
    this.requestTableFormService.resetForm(this.editForm, requestTable);

    this.notificationTablesCollection = this.notificationTableService.addNotificationTableToCollectionIfMissing<INotificationTable>(
      this.notificationTablesCollection,
      requestTable.notificationTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.notificationTableService
      .query({ filter: 'requesttable-is-null' })
      .pipe(map((res: HttpResponse<INotificationTable[]>) => res.body ?? []))
      .pipe(
        map((notificationTables: INotificationTable[]) =>
          this.notificationTableService.addNotificationTableToCollectionIfMissing<INotificationTable>(
            notificationTables,
            this.requestTable?.notificationTable,
          ),
        ),
      )
      .subscribe((notificationTables: INotificationTable[]) => (this.notificationTablesCollection = notificationTables));
  }
}
