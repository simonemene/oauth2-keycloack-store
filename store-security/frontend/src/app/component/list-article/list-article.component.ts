import { Component, inject } from '@angular/core';
import { ArticleService } from '../../service/article.service';
import { ListArticleDto } from '../../model/ListArticleDto';
import { StockDto } from '../../model/StockDto';
import { StockService } from '../../service/stock.service';
import { StockArticleDto } from '../../model/StockArticleDto';
import { AllStockDto } from '../../model/AllStockDto';
import { AlertComponent } from '../../shared/component/alert/alert.component';

@Component({
  selector: 'app-list-article',
  standalone: true,
  imports: [AlertComponent],
  templateUrl: './list-article.component.html',
  styleUrl: './list-article.component.scss'
})
export class ListArticleComponent {

  articleService = inject(ArticleService);
  stockService = inject(StockService);
  articles:StockArticleDto[] = [];
  message:string = '';
  modifyError:boolean=false;


  constructor()
  {
    this.loadArticle();
  }

  quantity(id:number)
  {  
    this.stockService.addQuantityArticle(id).subscribe(
      {
         next:(result:AllStockDto)=>
         {
          this.loadArticle();
          this.message = "ARTICLE MODIFY";
          this.modifyError = false;
         },
         error:err=>
         {
            this.message = err.error;
            this.modifyError = true;
         }
      }
    )
  }

  minus(id:number)
  {
    this.stockService.minusQuantityArticle(id).subscribe(
      {
        next:(result:AllStockDto)=>
        {
          this.loadArticle();
          this.message = "ARTICLE MODIFY";
          this.modifyError = false;
        },
         error:err=>
         {
            this.message = err.error;
            this.modifyError = true;
         }
      }
    )
  }

  private loadArticle()
  {
     this.stockService.allArticleInStockWithQuantity().subscribe(
      {
        next:(artilces:StockArticleDto[])=>
        {         
          this.articles = artilces;
        },
        error:err=>console.error(err) 
      }
    )
  }



}
