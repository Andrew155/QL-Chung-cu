import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IRoomTable } from 'app/entities/room-table/room-table.model';
import { RoomTableService } from 'app/entities/room-table/service/room-table.service';
import { DonationTableService } from '../service/donation-table.service';
import { IDonationTable } from '../donation-table.model';
import { DonationTableFormService } from './donation-table-form.service';

import { DonationTableUpdateComponent } from './donation-table-update.component';

describe('DonationTable Management Update Component', () => {
  let comp: DonationTableUpdateComponent;
  let fixture: ComponentFixture<DonationTableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let donationTableFormService: DonationTableFormService;
  let donationTableService: DonationTableService;
  let roomTableService: RoomTableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DonationTableUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(DonationTableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DonationTableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    donationTableFormService = TestBed.inject(DonationTableFormService);
    donationTableService = TestBed.inject(DonationTableService);
    roomTableService = TestBed.inject(RoomTableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call RoomTable query and add missing value', () => {
      const donationTable: IDonationTable = { id: 456 };
      const roomTable: IRoomTable = { id: 20989 };
      donationTable.roomTable = roomTable;

      const roomTableCollection: IRoomTable[] = [{ id: 22158 }];
      jest.spyOn(roomTableService, 'query').mockReturnValue(of(new HttpResponse({ body: roomTableCollection })));
      const additionalRoomTables = [roomTable];
      const expectedCollection: IRoomTable[] = [...additionalRoomTables, ...roomTableCollection];
      jest.spyOn(roomTableService, 'addRoomTableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ donationTable });
      comp.ngOnInit();

      expect(roomTableService.query).toHaveBeenCalled();
      expect(roomTableService.addRoomTableToCollectionIfMissing).toHaveBeenCalledWith(
        roomTableCollection,
        ...additionalRoomTables.map(expect.objectContaining),
      );
      expect(comp.roomTablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const donationTable: IDonationTable = { id: 456 };
      const roomTable: IRoomTable = { id: 19519 };
      donationTable.roomTable = roomTable;

      activatedRoute.data = of({ donationTable });
      comp.ngOnInit();

      expect(comp.roomTablesSharedCollection).toContain(roomTable);
      expect(comp.donationTable).toEqual(donationTable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTable>>();
      const donationTable = { id: 123 };
      jest.spyOn(donationTableFormService, 'getDonationTable').mockReturnValue(donationTable);
      jest.spyOn(donationTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donationTable }));
      saveSubject.complete();

      // THEN
      expect(donationTableFormService.getDonationTable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(donationTableService.update).toHaveBeenCalledWith(expect.objectContaining(donationTable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTable>>();
      const donationTable = { id: 123 };
      jest.spyOn(donationTableFormService, 'getDonationTable').mockReturnValue({ id: null });
      jest.spyOn(donationTableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: donationTable }));
      saveSubject.complete();

      // THEN
      expect(donationTableFormService.getDonationTable).toHaveBeenCalled();
      expect(donationTableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDonationTable>>();
      const donationTable = { id: 123 };
      jest.spyOn(donationTableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ donationTable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(donationTableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareRoomTable', () => {
      it('Should forward to roomTableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(roomTableService, 'compareRoomTable');
        comp.compareRoomTable(entity, entity2);
        expect(roomTableService.compareRoomTable).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
