import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, signal } from '@angular/core';
import { environment } from '../../environments/environment';
import { AllUserDto } from '../model/AllUserDto';
import { URL } from '../constant/url.constants';
import { Observable } from 'rxjs';
import { UserDto } from '../model/UserDto';

@Injectable({
  providedIn: 'root'
})
export class UserService {

   private _user = signal(false);

   private _singleUser = signal(false);

   setRealoadUser(value:boolean)
   {
    this._user.set(value);
   }

   get reloadUsers() {
    return this._user;
  }

  setRealoadUserSingle(value:boolean)
   {
    this._singleUser.set(value);
   }

   get reloadUsersSingle() {
    return this._singleUser;
  }

  baseUrl:string = environment.apiBaseUrl;

  constructor(private http:HttpClient) { }

  allUser():Observable<AllUserDto>
  {
    return this.http.get<AllUserDto>(this.baseUrl + URL.ALLUSER);
  }

  getProfile(id:number):Observable<UserDto>
  {
    let params = new HttpParams();
    params.append("id",id);
    return this.http.get<UserDto>(`${this.baseUrl}${URL.ALLUSER}/${id}`,{params});
  }

  updateProfile(id:number,user:UserDto)
  {
    return this.http.put<UserDto>(`${this.baseUrl}${URL.ALLUSER}/${id}`,user);
  }
}
