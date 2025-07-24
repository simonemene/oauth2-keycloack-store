import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { UserDto } from '../model/UserDto'
import { URL } from '../constant/url.constants';
import { SessionStorageService } from './session-storage.service';
import { LoginUserJwt } from '../model/LoginUserJwt';
import { LoginUser } from '../model/LoginUser';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  urlBase = environment.apiBaseUrl;

  constructor(private http:HttpClient) { }


  authentication(user:LoginUser):Observable<LoginUserJwt>
  {
    return this.http.post<LoginUserJwt>(this.urlBase + URL.AUTH,user);
  }

  registration(user:UserDto):Observable<UserDto>
  {
    return this.http.post<UserDto>(this.urlBase + URL.REGISTRATION,user);
  }

  getUser():Observable<UserDto>
  {   
    return this.http.get<UserDto>(`${this.urlBase}${URL.USER}`);
  }
}
