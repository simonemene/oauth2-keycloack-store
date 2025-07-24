import { Component, inject, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TrackService } from '../../service/track.service';
import { TrackDto } from '../../model/TrackDto';

@Component({
  selector: 'app-manage-track',
  standalone: true,
  imports: [],
  templateUrl: './manage-track.component.html',
  styleUrl: './manage-track.component.scss'
})
export class ManageTrackComponent{

  activatedRouter = inject(ActivatedRoute);
  trackService = inject(TrackService);
  idOrder!:number;
  track:TrackDto = new TrackDto();


  constructor()
  {
    this.activatedRouter.params.subscribe(param=>
      {
        this.idOrder = param['idOrder'];
        this.trackService.getTrack(this.idOrder).subscribe(
          {
            next:(result:TrackDto)=>
            {
              this.track = result;
            }
          }
        )

      }
    )
  }

}
