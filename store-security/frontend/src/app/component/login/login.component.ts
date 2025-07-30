import { Component, inject } from '@angular/core';
import { AuthenticationService } from '../../service/authentication.service';
import { UserDto } from '../../model/UserDto';
import { FormControl, FormGroup, NgForm, ReactiveFormsModule, Validators } from '@angular/forms';
import { Route, Router, RouterModule } from '@angular/router';
import { SessionStorageService } from '../../service/session-storage.service';
import { getCookie } from 'typescript-cookie';
import { AlertComponent } from '../../shared/component/alert/alert.component';
import { LoginUserJwt } from '../../model/LoginUserJwt';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,AlertComponent],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  user!:UserDto;
  storeForm!:FormGroup;
  errorAuthentication:boolean=false;
  sessionStorageAuth = inject(SessionStorageService);
  errorMessage:string = 'Username or Password incorrect';

  constructor(private auth:AuthenticationService,private router:Router)
  {
    this.user = new UserDto();
    this.storeForm = new FormGroup(
      {
        email: new FormControl('',[Validators.required,Validators.email]),
        password : new FormControl('',[Validators.required])
      }
    )
  }

  onSubmit()
  {
    this.user.username=this.storeForm.value.email;
    this.user.password=this.storeForm.value.password;

    this.auth.authentication(this.user).subscribe(
      {
        next:(responseData:LoginUserJwt)=>
        {         
          this.errorAuthentication=false;
          window.sessionStorage.setItem('Authorization',responseData.jwt);
          this.sessionStorageAuth.login();
          this.router.navigate(['/welcome']);
        },
        error:(err)=>
        {
          if(err.status === 401)
          {
            this.errorAuthentication=true;
          }
        }
      }
    )
  }
}
