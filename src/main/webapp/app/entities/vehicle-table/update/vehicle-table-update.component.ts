import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { IVehicleTable } from '../vehicle-table.model';
import { VehicleTableService } from '../service/vehicle-table.service';
import { VehicleTableFormService, VehicleTableFormGroup } from './vehicle-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-vehicle-table-update',
  templateUrl: './vehicle-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class VehicleTableUpdateComponent implements OnInit {
  isSaving = false;
  vehicleTable: IVehicleTable | null = null;

  roomTablesSharedCollection: IRoomTable[] = [];

  protected vehicleTableService = inject(VehicleTableService);
  protected vehicleTableFormService = inject(VehicleTableFormService);
  protected roomTableService = inject(RoomTableService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: VehicleTableFormGroup = this.vehicleTableFormService.createVehicleTableFormGroup();

  compareRoomTable = (o1: IRoomTable | null, o2: IRoomTable | null): boolean => this.roomTableService.compareRoomTable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleTable }) => {
      this.vehicleTable = vehicleTable;
      if (vehicleTable) {
        this.updateForm(vehicleTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleTable = this.vehicleTableFormService.getVehicleTable(this.editForm);
    if (vehicleTable.id !== null) {
      this.subscribeToSaveResponse(this.vehicleTableService.update(vehicleTable));
    } else {
      this.subscribeToSaveResponse(this.vehicleTableService.create(vehicleTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleTable>>): void {
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

  protected updateForm(vehicleTable: IVehicleTable): void {
    this.vehicleTable = vehicleTable;
    this.vehicleTableFormService.resetForm(this.editForm, vehicleTable);

    this.roomTablesSharedCollection = this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(
      this.roomTablesSharedCollection,
      vehicleTable.roomTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomTableService
      .query()
      .pipe(map((res: HttpResponse<IRoomTable[]>) => res.body ?? []))
      .pipe(
        map((roomTables: IRoomTable[]) =>
          this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(roomTables, this.vehicleTable?.roomTable),
        ),
      )
      .subscribe((roomTables: IRoomTable[]) => (this.roomTablesSharedCollection = roomTables));
  }
}
