import { Component, inject, Input, OnInit } from '@angular/core';
import { TrackDto } from '../../model/TrackDto';
import { ActivatedRoute } from '@angular/router';
import { TrackService } from '../../service/track.service';

@Component({
  selector: 'app-user-track-page',
  standalone: true,
  imports: [],
  templateUrl: './user-track-page.component.html',
  styleUrl: './user-track-page.component.scss',
})
export class UserTrackPageComponent implements OnInit {
  activatedRouter = inject(ActivatedRoute);
  trackService = inject(TrackService);
  track: TrackDto = new TrackDto();

  @Input('idOrder') idOrder!: number;

  constructor() {}

  ngOnInit(): void {
    this.trackService.getTrack(this.idOrder).subscribe({
      next: (result: TrackDto) => {
        this.track = result;
      },
    });
  }
}
