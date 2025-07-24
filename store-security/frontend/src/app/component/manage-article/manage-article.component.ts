import { Component, inject, OnInit } from '@angular/core';
import { StockService } from '../../service/stock.service';
import { ArticleService } from '../../service/article.service';
import { StockDto } from '../../model/StockDto';
import { FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ArticleDto } from '../../model/ArticleDto';
import { ListArticleDto } from '../../model/ListArticleDto';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-manage-article',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, CommonModule],
  templateUrl: './manage-article.component.html',
  styleUrl: './manage-article.component.scss'
})
export class ManageArticleComponent implements OnInit {

  articleService = inject(ArticleService);

  articleForm!: FormGroup;
  articleDto: ArticleDto = new ArticleDto();
  articles: ListArticleDto = new ListArticleDto();
  existArticle: boolean = true;
  messageArticle: string = '';

  saveArticle: boolean = false;

  constructor() { }


  ngOnInit(): void {
    this.articleForm = new FormGroup(
      {
        name: new FormControl('', Validators.required),
        description: new FormControl('', Validators.required),
        price: new FormControl(0, [Validators.max(100), Validators.min(1), Validators.required])
      }
    )
  }

  onSubmit() {
    this.articleDto.description = this.articleForm.value.description;
    this.articleDto.name = this.articleForm.value.name;
    this.articleDto.price = this.articleForm.value.price;
    this.articleService.getAllArticle().subscribe(
      {
        next: (artilces: ListArticleDto) => {
          this.articles = artilces;
          if (this.articles && this.articles.articles && this.articles.articles.length != 0) {
            this.existArticle = this.articles.articles.some(article => article.name === this.articleDto.name);
          }else
          {
            this.existArticle = false;
          }
          if (!this.existArticle) {
            this.articleService.addArticle(this.articleDto).subscribe(
              {
                next: (articleInsert: ArticleDto) => {
                  this.saveArticle = true;
                  this.articleForm.get('name')?.setValue('');
                  this.articleForm.get('description')?.setValue('');
                  this.articleForm.get('price')?.setValue('');
                  this.messageArticle = `SAVED ARTICLE : ${this.articleDto.name} ${this.articleDto.description} ${this.articleDto.price}`;
                },
                error: err => {
                  console.error(err);
                  this.saveArticle = false;
                  this.messageArticle = 'ARTICLE ERROR';
                }
              }
            )
          } else {
            this.saveArticle = false;
            this.messageArticle = `ARTICLE EXISTS`
          }

        },
        error: err => console.error(err)
      }
    )


  }







}
