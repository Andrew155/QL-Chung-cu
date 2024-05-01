import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { IFeeTable } from '../fee-table.model';
import { FeeTableService } from '../service/fee-table.service';
import { FeeTableFormService, FeeTableFormGroup } from './fee-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-fee-table-update',
  templateUrl: './fee-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class FeeTableUpdateComponent implements OnInit {
  isSaving = false;
  feeTable: IFeeTable | null = null;

  roomTablesSharedCollection: IRoomTable[] = [];

  protected feeTableService = inject(FeeTableService);
  protected feeTableFormService = inject(FeeTableFormService);
  protected roomTableService = inject(RoomTableService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: FeeTableFormGroup = this.feeTableFormService.createFeeTableFormGroup();

  compareRoomTable = (o1: IRoomTable | null, o2: IRoomTable | null): boolean => this.roomTableService.compareRoomTable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feeTable }) => {
      this.feeTable = feeTable;
      if (feeTable) {
        this.updateForm(feeTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feeTable = this.feeTableFormService.getFeeTable(this.editForm);
    if (feeTable.id !== null) {
      this.subscribeToSaveResponse(this.feeTableService.update(feeTable));
    } else {
      this.subscribeToSaveResponse(this.feeTableService.create(feeTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeeTable>>): void {
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

  protected updateForm(feeTable: IFeeTable): void {
    this.feeTable = feeTable;
    this.feeTableFormService.resetForm(this.editForm, feeTable);

    this.roomTablesSharedCollection = this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(
      this.roomTablesSharedCollection,
      feeTable.roomTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomTableService
      .query()
      .pipe(map((res: HttpResponse<IRoomTable[]>) => res.body ?? []))
      .pipe(
        map((roomTables: IRoomTable[]) =>
          this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(roomTables, this.feeTable?.roomTable),
        ),
      )
      .subscribe((roomTables: IRoomTable[]) => (this.roomTablesSharedCollection = roomTables));
  }
}
