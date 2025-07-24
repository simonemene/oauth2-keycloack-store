import { ArticleDto } from "./ArticleDto";

export class StockArticleDto
{
    id!:number;
    stockId!:number;
    article!:ArticleDto;
    quantity!:number;
}