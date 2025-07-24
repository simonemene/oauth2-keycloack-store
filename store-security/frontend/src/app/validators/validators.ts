import { AbstractControl, ValidationErrors, ValidatorFn } from "@angular/forms";

export const passwordMatcher: ValidatorFn = (control: AbstractControl): ValidationErrors | null =>
{
    const password = control.get('password')?.value;
    const repeatpassword = control.get('repeatpassword')?.value;
    return password === repeatpassword ? null : {passwordsMismatch:true};
}