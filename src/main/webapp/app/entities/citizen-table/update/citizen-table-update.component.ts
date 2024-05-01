import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { ICitizenTable } from '../citizen-table.model';
import { CitizenTableService } from '../service/citizen-table.service';
import { CitizenTableFormService, CitizenTableFormGroup } from './citizen-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-citizen-table-update',
  templateUrl: './citizen-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CitizenTableUpdateComponent implements OnInit {
  isSaving = false;
  citizenTable: ICitizenTable | null = null;

  familyIdsCollection: IRoomTable[] = [];

  protected citizenTableService = inject(CitizenTableService);
  protected citizenTableFormService = inject(CitizenTableFormService);
  protected roomTableService = inject(RoomTableService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CitizenTableFormGroup = this.citizenTableFormService.createCitizenTableFormGroup();

  compareRoomTable = (o1: IRoomTable | null, o2: IRoomTable | null): boolean => this.roomTableService.compareRoomTable(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ citizenTable }) => {
      this.citizenTable = citizenTable;
      if (citizenTable) {
        this.updateForm(citizenTable);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const citizenTable = this.citizenTableFormService.getCitizenTable(this.editForm);
    if (citizenTable.id !== null) {
      this.subscribeToSaveResponse(this.citizenTableService.update(citizenTable));
    } else {
      this.subscribeToSaveResponse(this.citizenTableService.create(citizenTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICitizenTable>>): void {
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

  protected updateForm(citizenTable: ICitizenTable): void {
    this.citizenTable = citizenTable;
    this.citizenTableFormService.resetForm(this.editForm, citizenTable);

    this.familyIdsCollection = this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(
      this.familyIdsCollection,
      citizenTable.familyId,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.roomTableService
      .query({ filter: 'citizentable-is-null' })
      .pipe(map((res: HttpResponse<IRoomTable[]>) => res.body ?? []))
      .pipe(
        map((roomTables: IRoomTable[]) =>
          this.roomTableService.addRoomTableToCollectionIfMissing<IRoomTable>(roomTables, this.citizenTable?.familyId),
        ),
      )
      .subscribe((roomTables: IRoomTable[]) => (this.familyIdsCollection = roomTables));
  }
}
