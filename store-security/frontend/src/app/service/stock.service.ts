import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { URL } from '../constant/url.constants';
import { map, Observable } from 'rxjs';
import { AllStockDto } from '../model/AllStockDto';
import { StockDto } from '../model/StockDto';
import { StockArticleDto } from '../model/StockArticleDto';

@Injectable({
  providedIn: 'root'
})
export class StockService {

  urlBase = environment.apiBaseUrl;

  constructor(private http: HttpClient) { }

  allArticleInStock(): Observable<StockDto[]> {
    return this.http.get<AllStockDto>(this.urlBase + URL.ALLSTOCK)
      .pipe(
        map(
          allStock => {
            return allStock.stock
          }
        )
      )
  }

  allArticleInStockWithQuantity(): Observable<StockArticleDto[]> {
    return this.http.get<StockDto>(this.urlBase + URL.ALLSTOCK)
      .pipe(
        map(
          allStock => {
            return allStock.stockArticles;
          }
        )
      )
  }

  addQuantityArticle(id: number) {
    let add = 1;
    return this.http.patch<AllStockDto>(`${this.urlBase}${URL.ALLSTOCK}/${id}/${add}`, null); 
  }

  minusQuantityArticle(id: number) { 
    let minus = 1;
    return this.http.patch<AllStockDto>(`${this.urlBase}${URL.ALLSTOCK}/${id}/decrement/${minus}`,null); 
  }
}
