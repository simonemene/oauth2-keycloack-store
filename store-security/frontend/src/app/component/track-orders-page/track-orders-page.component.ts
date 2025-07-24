import { Component, inject } from '@angular/core';
import { UserService } from '../../service/user.service';
import { OrderService } from '../../service/order.service';
import { TrackService } from '../../service/track.service';
import { AllUserDto } from '../../model/AllUserDto';
import { AllOrderDto } from '../../model/AllOrderDto';
import { TrackDto } from '../../model/TrackDto';
import { AllTrackDto } from '../../model/AllTrackDto';
import { TrackUsersDto } from '../../model/TrackUsersDto';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SuccessComponent } from '../../shared/component/success/success.component';

@Component({
  selector: 'app-track-orders-page',
  standalone: true,
  imports: [FormsModule, CommonModule,SuccessComponent],
  templateUrl: './track-orders-page.component.html',
  styleUrl: './track-orders-page.component.scss',
})
export class TrackOrdersPageComponent {
  userService = inject(UserService);
  orderService = inject(OrderService);
  trackService = inject(TrackService);

  allUser: AllUserDto = new AllUserDto();
  allOrder: AllOrderDto = new AllOrderDto();
  allTrack: TrackUsersDto = new TrackUsersDto();

  successModTrack: boolean = false;
  messageSuccess: string = '';

  status: string = '';

  constructor() {
    this.getAllTrack();
  }

  statusMod(idTrack: number) {
    let track = new TrackDto();
    let idOrder = 0;
    for (let i = 0; i < this.allTrack.allTrack.length; i++) {
      for (let j = 0; j < this.allTrack.allTrack[i].track.length; j++) {
        if (this.allTrack.allTrack[i].track[j].id === idTrack) {
          let oldTrack = this.allTrack.allTrack[i].track[j];
          track = oldTrack;
          idOrder = track.order.id;
        }
      }
    }

    if (idOrder) {
      this.trackService.setTrack(idOrder, track).subscribe({
        next: (result: TrackDto) => {
          this.successModTrack = true;
          this.messageSuccess = `Success modify ORDER: ${idOrder}`
          this.getAllTrack();
        },
      });
    }
  }

  getAllTrack() {
    let allTrackCopy = new TrackUsersDto();
    this.userService.allUser().subscribe({
      next: (result: AllUserDto) => {
        this.allUser = result;
        for (let i = 0; i < this.allUser.users.length; i++) {
          let userTracker = new AllTrackDto();
          userTracker.username = this.allUser.users[i].username;
          if(!this.allUser.users[i].username.includes("track") && !this.allUser.users[i].username.includes("admin") )
          {
          this.orderService
            .getAllOrderUser(this.allUser.users[i].id)
            .subscribe({
              next: (orders: AllOrderDto) => {
                this.allOrder = orders;

                if (orders.orders && orders.orders.length > 0) {
                  for (let j = 0; j < orders.orders.length; j++) {
                    if (orders.orders[j]) {
                      this.trackService
                        .getTrack(orders.orders[j].idOrder)
                        .subscribe({
                          next: (track: TrackDto) => {
                            userTracker.track.push(track);
                          },
                          error: (err) => console.error(err),
                        });
                    }
                  }
                }
              },
            });
          allTrackCopy.allTrack.push(userTracker);
        }
      }
    },
    });
    this.allTrack = allTrackCopy;
  }
}
