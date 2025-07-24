import { Component, Inject, inject, Input, OnInit } from '@angular/core';
import { StockArticleDto } from '../../model/StockArticleDto';
import { OrderService } from '../../service/order.service';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { ArticlesOrderDto } from '../../model/ArticlesOrderDto';
import { AllArticleOrderDto } from '../../model/AllArticleOrderDto';
import { AuthenticationService } from '../../service/authentication.service';
import { UserDto } from '../../model/UserDto';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [MatDialogModule, CommonModule],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent implements OnInit {

  articles: StockArticleDto[] = [];
  orderService = inject(OrderService);
  authService = inject(AuthenticationService);
  total: number = 0;

  constructor(@Inject(MAT_DIALOG_DATA) public data: StockArticleDto[], private dialogRef: MatDialogRef<CartComponent>) {
    this.articles = data;
  }

  ngOnInit(): void {
    this.calculatePrice();
  }

  removeItem(article: StockArticleDto) {
    let index = this.articles.indexOf(article);
    this.articles.splice(index, 1);
    this.calculatePrice();
  }

  private calculatePrice() {
    for (let i = 0; i < this.articles.length; i++) {
      this.total += this.articles[i].article.price;
    }
  }

  closeDialogNoCheckout() {

    this.dialogRef.close(
      {
        articles: this.articles,
        checkout: false
      }
    )
  }

  closeDialogWithCheckout() {

    this.dialogRef.close(
      {
        articles: this.articles,
        checkout: true
      }
    )
  }

  order() {
    let user = '';
    this.authService.getUser().subscribe(
      {
        next: (username: UserDto) => {

          
          user = username.username;

          //order
          let orderArticle = new ArticlesOrderDto();
          orderArticle.articles = [];
          for (let i = 0; i < this.articles.length; i++) {
            let allArticle = new AllArticleOrderDto();
            allArticle.articleDto = this.articles[i].article;
            allArticle.quantity = this.articles[i].quantity;
            orderArticle.articles.push(allArticle);
            orderArticle.username = user;
            orderArticle.idOrder = 0;
          }
          this.orderService.order(orderArticle).subscribe(
            {
              next: (result: ArticlesOrderDto) => {
                this.closeDialogWithCheckout();
              },
              error: err => {
                console.error(err);
              }
            }
          )
        },
        error: err => {
          console.error(err);
        }
      }
    )

  }

}
