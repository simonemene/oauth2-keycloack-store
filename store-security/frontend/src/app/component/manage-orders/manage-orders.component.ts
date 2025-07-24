import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterOutlet } from '@angular/router';
import { OrderService } from '../../service/order.service';
import { AllOrderDto } from '../../model/AllOrderDto';
import { AlertComponent } from '../../shared/component/alert/alert.component';

@Component({
  selector: 'app-manage-orders',
  standalone: true,
  imports: [AlertComponent,RouterOutlet],
  templateUrl: './manage-orders.component.html',
  styleUrl: './manage-orders.component.scss',
})
export class ManageOrdersComponent implements OnInit {
  activatedRoute = inject(ActivatedRoute);
  orderService = inject(OrderService);
  router = inject(Router);

  username: string = '';
  orders: AllOrderDto = new AllOrderDto();

  modifyError: boolean = false;
  message: string = '';

  constructor() {}

  ngOnInit(): void {
    this.activatedRoute.params.subscribe((param) => {
      this.username = param['id'];
      this.orderService.getAllOrderUser(Number(this.username)).subscribe({
        next: (orders: AllOrderDto) => {
          this.orders = orders;
        },
        error: (err) => {
          console.error(err);
          this.modifyError = true;
          this.message = 'ORDER NOT FOUND';
        },
      });
    });
  }

  track(idOrder:number)
  {
    this.router.navigate(['/users', 'orders', this.username, idOrder, 'track']);
  }
}
