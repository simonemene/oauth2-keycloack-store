import { OrderDto } from "./OrderDto";

export class TrackDto
{
    id!:number;

    order!:OrderDto;

    status!:string;
}