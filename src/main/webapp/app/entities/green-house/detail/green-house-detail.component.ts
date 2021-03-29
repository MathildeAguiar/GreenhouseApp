import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGreenHouse } from '../green-house.model';

@Component({
  selector: 'jhi-green-house-detail',
  templateUrl: './green-house-detail.component.html',
})
export class GreenHouseDetailComponent implements OnInit {
  greenHouse: IGreenHouse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ greenHouse }) => {
      this.greenHouse = greenHouse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
