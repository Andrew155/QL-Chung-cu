import { Component, inject, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IRoomTable } from '../room-table.model';
import { RoomTableService } from '../service/room-table.service';
import { RoomTableFormService, RoomTableFormGroup } from './room-table-form.service';

@Component({
  standalone: true,
  selector: 'jhi-room-table-update',
  templateUrl: './room-table-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class RoomTableUpdateComponent implements OnInit {
  isSaving = false;
  roomTable: IRoomTable | null = null;

  protected roomTableService = inject(RoomTableService);
  protected roomTableFormService = inject(RoomTableFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: RoomTableFormGroup = this.roomTableFormService.createRoomTableFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ roomTable }) => {
      this.roomTable = roomTable;
      if (roomTable) {
        this.updateForm(roomTable);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const roomTable = this.roomTableFormService.getRoomTable(this.editForm);
    if (roomTable.id !== null) {
      this.subscribeToSaveResponse(this.roomTableService.update(roomTable));
    } else {
      this.subscribeToSaveResponse(this.roomTableService.create(roomTable));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRoomTable>>): void {
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

  protected updateForm(roomTable: IRoomTable): void {
    this.roomTable = roomTable;
    this.roomTableFormService.resetForm(this.editForm, roomTable);
  }
}
