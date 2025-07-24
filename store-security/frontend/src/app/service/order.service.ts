import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { URL } from '../constant/url.constants';
import { AllOrderDto } from '../model/AllOrderDto';
import { ArticlesOrderDto } from '../model/ArticlesOrderDto';

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  urlBase:string = environment.apiBaseUrl;

  constructor(private http:HttpClient) { }

  getAllOrderUser(id:number)
  {
     return this.http.get<AllOrderDto>(`${this.urlBase}${URL.ALLORDER}/${id}`);
  }

  order(articles:ArticlesOrderDto)
  {
    console.log(articles);
    
    return this.http.post<ArticlesOrderDto>(`${this.urlBase}${URL.ALLORDER}`,articles)
  }


}
