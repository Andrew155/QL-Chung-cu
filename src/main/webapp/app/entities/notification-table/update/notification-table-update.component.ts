import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { INotificationTable } from '../notification-table.model';
import { NotificationTableService } from '../service/notification-table.service';
import { NotificationTableFormService, NotificationTableFormGroup } from './notification-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-notification-table-update',
  templateUrl: './notification-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class NotificationTableUpdateComponent implements OnInit {
  isSaving = false;
  notificationTable: INotificationTable | null = null;

  applicationUsersSharedCollection: IApplicationUser[] = [];

  protected notificationTableService = inject(NotificationTableService);
  protected notificationTableFormService = inject(NotificationTableFormService);
  protected applicationUserService = inject(ApplicationUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: NotificationTableFormGroup = this.notificationTableFormService.createNotificationTableFormGroup();

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ notificationTable }) => {
      this.notificationTable = notificationTable;
      if (notificationTable) {
        this.updateForm(notificationTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const notificationTable = this.notificationTableFormService.getNotificationTable(this.editForm);
    if (notificationTable.id !== null) {
      this.subscribeToSaveResponse(this.notificationTableService.update(notificationTable));
    } else {
      this.subscribeToSaveResponse(this.notificationTableService.create(notificationTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INotificationTable>>): void {
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

  protected updateForm(notificationTable: INotificationTable): void {
    this.notificationTable = notificationTable;
    this.notificationTableFormService.resetForm(this.editForm, notificationTable);

    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      notificationTable.applicationTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.notificationTable?.applicationTable,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
