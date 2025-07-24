import { Component, effect, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UserDto } from '../../model/UserDto';
import { UserService } from '../../service/user.service';
import { AuthenticationService } from '../../service/authentication.service';
import { SuccessComponent } from '../../shared/component/success/success.component';
import { Location } from '@angular/common';

@Component({
  selector: 'app-manage-profile',
  standalone: true,
  imports: [ReactiveFormsModule, FormsModule, SuccessComponent],
  templateUrl: './manage-profile.component.html',
  styleUrl: './manage-profile.component.scss',
})
export class ManageProfileComponent implements OnInit {
  activatedRoute = inject(ActivatedRoute);
  userService = inject(UserService);
  authUser = inject(AuthenticationService);

  username: string = '';
  userDto: UserDto = new UserDto();
  admin: boolean = false;


  messageSuccess: string = '';
  successUpdate: boolean = false;

  profileForm!: FormGroup;
  

  constructor(private location: Location, private router:Router) {


    effect(() => {      
      if (this.userService.reloadUsers()) {
        this.userService.setRealoadUser(false);
        router.navigate(['/logout']);
      }
    }, { allowSignalWrites: true });

    this.profileForm = new FormGroup({
      username: new FormControl(this.userDto.username, [Validators.email]),
      age: new FormControl(this.userDto.age, [
        Validators.pattern('^[0-9]*$'),
        Validators.min(18),
        Validators.max(99),
      ]),
    });
    this.admin = this.activatedRoute.snapshot.data['admin'];
  }

  ngOnInit(): void {
    if (this.admin) {
      this.activatedRoute.params.subscribe((params) => {
        this.username = params['id'];
        this.userService.getProfile(Number(this.username)).subscribe({
          next: (userDate: UserDto) => {
            this.userDto = userDate;
          },
          error: (err) => console.error(err),
        });
      });
    } else {
      this.authUser.getUser().subscribe({
        next: (user: UserDto) => {
          this.userDto = user;
        },
        error: (err) => console.error(err),
      });
    }
  }

  onSubmit() {
    if (this.profileForm.value.username) {
      this.userDto.username = this.profileForm.value.username;
    }
    if (this.profileForm.value.age) {
      this.userDto.age = this.profileForm.value.age;
    }

    this.userService.updateProfile(this.userDto.id, this.userDto).subscribe({
      next: (result: UserDto) => {
        this.successUpdate = true;
        this.messageSuccess = 'UPDATE PROFILE';
        this.userService.setRealoadUser(true);
        this.location.back();
        this.userService.getProfile(this.userDto.id).subscribe({
          next: (userDate: UserDto) => {
            this.userDto = userDate;
          },
          error: (err) => console.error(err),
        });
      },
    });
  }
}
