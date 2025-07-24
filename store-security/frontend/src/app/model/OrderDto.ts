import { UserDto } from "./UserDto";

export class OrderDto 
{
    id!:number;

    user!:UserDto;

    tmstInsert!:Date;
}