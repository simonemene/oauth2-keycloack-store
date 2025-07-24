import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { URL } from '../constant/url.constants';
import { TrackDto } from '../model/TrackDto';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TrackService {
  urlBase: string = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  getTrack(idOrder: number) {
    return this.http.get<TrackDto>(
      `${this.urlBase}${URL.ALLTRACKBYID}/${idOrder}`
    );
  }

  setTrack(idOrder: number, track: TrackDto) {
    return this.http.put<TrackDto>(
      `${this.urlBase}${URL.ALLTRACKBYID}/${idOrder}`,
      track
    );
  }
}
