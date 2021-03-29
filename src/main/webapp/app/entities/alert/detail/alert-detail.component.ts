import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAlert } from '../alert.model';

@Component({
  selector: 'jhi-alert-detail',
  templateUrl: './alert-detail.component.html',
})
export class AlertDetailComponent implements OnInit {
  alert: IAlert | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alert }) => {
      this.alert = alert;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
