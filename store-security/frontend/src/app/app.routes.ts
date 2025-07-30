import { Routes } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { WelcomeComponent } from './component/welcome/welcome.component';
import { LoginComponent } from './component/login/login.component';
import { LogoutComponent } from './component/logout/logout.component';
import { authenticationGuard } from './guard/authentication.guard';
import { roleGuard } from './guard/role.guard';
import { ROLE } from './constant/role.constants';
import { RegisterComponent } from './component/register/register.component';
import { ManageUsersComponent } from './component/manage-users/manage-users.component';
import { ManageProfileComponent } from './component/manage-profile/manage-profile.component';
import { ManageOrdersComponent } from './component/manage-orders/manage-orders.component';
import { ManageOrderComponent } from './component/manage-order/manage-order.component';
import { UserProfilePageComponent } from './component/user-profile-page/user-profile-page.component';
import { ManageArticleComponent } from './component/manage-article/manage-article.component';
import { ListArticleComponent } from './component/list-article/list-article.component';
import { UserArticlesPageComponent } from './component/user-articles-page/user-articles-page.component';
import { UserOrdersPageComponent } from './component/user-orders-page/user-orders-page.component';
import { ManageTrackComponent } from './component/manage-track/manage-track.component';
import { TrackOrdersPageComponent } from './component/track-orders-page/track-orders-page.component';

export const routes: Routes = [
    {
        path: '', component: HomeComponent, pathMatch: 'full'
    },
    {
        path: 'welcome', component: WelcomeComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.USER, ROLE.ADMIN,ROLE.TRACK] }
    },
    {
        path: 'login', component: LoginComponent
    },
    {
        path: 'logout', component: LogoutComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.USER, ROLE.ADMIN,ROLE.TRACK] }
    },
    {
        path: 'signup', component: RegisterComponent
    },
    {
        path: 'users', component: ManageUsersComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.ADMIN] },
        children: [
            {
                path: ':id',
                component: ManageProfileComponent,
                data: { admin: true }
            },
            {
                path: 'orders/:id',
                component: ManageOrdersComponent,
                children:
                [
                    {
                        path: ':idOrder/track',
                        component:ManageTrackComponent
                    }
                ]
            }
        ]
    },
    {
        path: 'article',
        component: ManageArticleComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.ADMIN] }
    },
    {
        path: 'articles',
        component: ListArticleComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.ADMIN] }

    },
    {
        path: 'user-page',
        component: ManageProfileComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.USER], admin: false },
    },
    {
        path: 'user-article',
        component: UserArticlesPageComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.USER], admin: false },
    },
    {
        path: 'user-orders-page',
        component: UserOrdersPageComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.USER], admin: false },
        children:
        [
            {
                path:':idOrder/track',
                component:ManageTrackComponent
            }
        ]
    },
    {
        path:'track',
        component:TrackOrdersPageComponent,
        canActivate: [authenticationGuard, roleGuard],
        data: { roles: [ROLE.TRACK]}
    },
    {
        path: '**', component: WelcomeComponent
    }
];
