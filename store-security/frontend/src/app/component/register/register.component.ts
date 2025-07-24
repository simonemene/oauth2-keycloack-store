import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { passwordMatcher } from '../../validators/validators';
import { UserDto } from '../../model/UserDto';
import { AuthenticationService } from '../../service/authentication.service';
import { Router } from '@angular/router';
import { AlertComponent } from '../../shared/component/alert/alert.component';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule,AlertComponent],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {

  errorRegister:boolean = false;
  messageErrorRegister:string='';

  registerForm!:FormGroup;
  user!:UserDto;

  authenticationService = inject(AuthenticationService);
  router = inject(Router);

  constructor()
  {
    this.registerForm = new FormGroup(
      {
        email: new FormControl('',[Validators.email,Validators.required]),
        age: new FormControl('',[Validators.required,Validators.min(18),Validators.max(100)]),
        password: new FormControl('', [
           Validators.required,
           Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')
        ]),
        repeatpassword: new FormControl('',[
           Validators.required
          ,Validators.pattern('^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$')
        ])
      },
      { validators : passwordMatcher}
    )
  }

  onSubmit()
  {
    if(this.registerForm.invalid)
    {
      this.errorRegister = true;
      this.messageErrorRegister = 'Non valid registration';
      return;
    }

    this.errorRegister = false;
    this.messageErrorRegister = '';
    this.user = new UserDto();
    this.user.username = this.registerForm.value.email;
    this.user.password = this.registerForm.value.password;
    this.user.age = this.registerForm.value.age;

    this.authenticationService.registration(this.user).subscribe(
      {
        next:(user:UserDto)=>
        {
           this.router.navigate(['/login']);
        },
        error:(err)=>
        {
           console.error(err);
           this.errorRegister = true;
           this.messageErrorRegister = err.error;
        }
      }
    )
  }
}
