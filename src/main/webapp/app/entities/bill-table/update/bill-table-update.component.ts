import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { IApplicationUser } from 'app/entities/application-user/application-user.model';
import { ApplicationUserService } from 'app/entities/application-user/service/application-user.service';
import { BillTableService } from '../service/bill-table.service';
import { IBillTable } from '../bill-table.model';
import { BillTableFormService, BillTableFormGroup } from './bill-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-bill-table-update',
  templateUrl: './bill-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BillTableUpdateComponent implements OnInit {
  isSaving = false;
  billTable: IBillTable | null = null;

  roomTablesSharedCollection: IRoomTable[] = [];
  applicationUsersSharedCollection: IApplicationUser[] = [];

  protected billTableService = inject(BillTableService);
  protected billTableFormService = inject(BillTableFormService);
  protected roomTableService = inject(RoomTableService);
  protected applicationUserService = inject(ApplicationUserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: BillTableFormGroup = this.billTableFormService.createBillTableFormGroup();

  compareRoomTable = (o1: IRoomTable | null, o2: IRoomTable | null): boolean => this.roomTableService.compareRoomTable(o1, o2);

  compareApplicationUser = (o1: IApplicationUser | null, o2: IApplicationUser | null): boolean =>
    this.applicationUserService.compareApplicationUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billTable }) => {
      this.billTable = billTable;
      if (billTable) {
        this.updateForm(billTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const billTable = this.billTableFormService.getBillTable(this.editForm);
    if (billTable.id !== null) {
      this.subscribeToSaveResponse(this.billTableService.update(billTable));
    } else {
      this.subscribeToSaveResponse(this.billTableService.create(billTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillTable>>): void {
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

  protected updateForm(billTable: IBillTable): void {
    this.billTable = billTable;
    this.billTableFormService.resetForm(this.editForm, billTable);

    this.roomTablesSharedCollection = this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(
      this.roomTablesSharedCollection,
      billTable.roomTable,
    );
    this.applicationUsersSharedCollection = this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
      this.applicationUsersSharedCollection,
      billTable.applicationTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomTableService
      .query()
      .pipe(map((res: HttpResponse<IRoomTable[]>) => res.body ?? []))
      .pipe(
        map((roomTables: IRoomTable[]) =>
          this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(roomTables, this.billTable?.roomTable),
        ),
      )
      .subscribe((roomTables: IRoomTable[]) => (this.roomTablesSharedCollection = roomTables));

    this.applicationUserService
      .query()
      .pipe(map((res: HttpResponse<IApplicationUser[]>) => res.body ?? []))
      .pipe(
        map((applicationUsers: IApplicationUser[]) =>
          this.applicationUserService.addApplicationUserToCollectionIfMissing<IApplicationUser>(
            applicationUsers,
            this.billTable?.applicationTable,
          ),
        ),
      )
      .subscribe((applicationUsers: IApplicationUser[]) => (this.applicationUsersSharedCollection = applicationUsers));
  }
}
