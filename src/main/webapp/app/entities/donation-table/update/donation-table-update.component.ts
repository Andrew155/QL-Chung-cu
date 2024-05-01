import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { IDonationTable } from '../donation-table.model';
import { DonationTableService } from '../service/donation-table.service';
import { DonationTableFormService, DonationTableFormGroup } from './donation-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-donation-table-update',
  templateUrl: './donation-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DonationTableUpdateComponent implements OnInit {
  isSaving = false;
  donationTable: IDonationTable | null = null;

  roomTablesSharedCollection: IRoomTable[] = [];

  protected donationTableService = inject(DonationTableService);
  protected donationTableFormService = inject(DonationTableFormService);
  protected roomTableService = inject(RoomTableService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: DonationTableFormGroup = this.donationTableFormService.createDonationTableFormGroup();

  compareRoomTable = (o1: IRoomTable | null, o2: IRoomTable | null): boolean => this.roomTableService.compareRoomTable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donationTable }) => {
      this.donationTable = donationTable;
      if (donationTable) {
        this.updateForm(donationTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const donationTable = this.donationTableFormService.getDonationTable(this.editForm);
    if (donationTable.id !== null) {
      this.subscribeToSaveResponse(this.donationTableService.update(donationTable));
    } else {
      this.subscribeToSaveResponse(this.donationTableService.create(donationTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDonationTable>>): void {
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

  protected updateForm(donationTable: IDonationTable): void {
    this.donationTable = donationTable;
    this.donationTableFormService.resetForm(this.editForm, donationTable);

    this.roomTablesSharedCollection = this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(
      this.roomTablesSharedCollection,
      donationTable.roomTable,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomTableService
      .query()
      .pipe(map((res: HttpResponse<IRoomTable[]>) => res.body ?? []))
      .pipe(
        map((roomTables: IRoomTable[]) =>
          this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(roomTables, this.donationTable?.roomTable),
        ),
      )
      .subscribe((roomTables: IRoomTable[]) => (this.roomTablesSharedCollection = roomTables));
  }
}
