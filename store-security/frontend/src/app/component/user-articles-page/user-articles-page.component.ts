import { Component, inject } from '@angular/core';
import { StockArticleDto } from '../../model/StockArticleDto';
import { AllStockDto } from '../../model/AllStockDto';
import { ArticleService } from '../../service/article.service';
import { StockService } from '../../service/stock.service';
import { ArticlesOrderDto } from '../../model/ArticlesOrderDto';
import { MatDialog } from '@angular/material/dialog';
import { CartComponent } from '../../shared/cart/cart.component';
import { AlertComponent } from '../../shared/component/alert/alert.component';
import { SuccessComponent } from '../../shared/component/success/success.component';

@Component({
  selector: 'app-user-articles-page',
  standalone: true,
  imports: [AlertComponent, SuccessComponent],
  templateUrl: './user-articles-page.component.html',
  styleUrl: './user-articles-page.component.scss',
})
export class UserArticlesPageComponent {
  stockService = inject(StockService);
  articles: StockArticleDto[] = [];
  message: string = '';
  modifyError: boolean = false;
  readonly dialog = inject(MatDialog);

  messageSuccess: string = '';
  successAddRemove: boolean = false;

  order: StockArticleDto[] = [];

  constructor() {
    this.loadArticle();
  }

  private loadArticle() {
    this.stockService.allArticleInStockWithQuantity().subscribe({
      next: (artilces: StockArticleDto[]) => {
        this.articles = artilces.filter((article) => article.quantity > 0);
      },
      error: (err) => console.error(err),
    });
  }

  add(article: StockArticleDto) {
    let index = this.order.indexOf(article);
    if (index >= 0) {
      this.modifyError = true;
      this.successAddRemove = false;
      this.message = 'You can only order one item';
    } else {
      this.modifyError = false;
      this.successAddRemove = true;
      this.order.push(article);
      this.messageSuccess = 'Add article';
    }
  }

  remove(article: StockArticleDto) {
    let index = this.order.indexOf(article);
    if (index >= 0) {
      this.modifyError = false;
      this.successAddRemove = true;
      this.order.splice(index, 1);
      this.messageSuccess = 'Remove article';
    } else {
      this.modifyError = true;
      this.successAddRemove = false;
      this.message = 'Article not added';
    }
  }

  checkoutArticle() {
    if (this.order.length > 0) {
      this.openDialog();
    } else {
      this.modifyError = true;
      this.message = 'ADD ARTICLE';
      this.successAddRemove = false;
    }
  }

  private openDialog() {
    const dialogRef = this.dialog.open(CartComponent, {
      data: this.order,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
        if (result.checkout) {
          this.order = [];
          this.modifyError = false;
          this.successAddRemove = true;
          this.messageSuccess = 'ORDER COMPLETED';
          this.loadArticle();
        } else {
          this.modifyError = false;
          this.successAddRemove = false;
          this.order = result.articles;
        }
      }
    });
  }
}
