import { Component } from '@angular/core';
import { ApiService } from '../services/api.service';

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss']
})
export class AuthComponent {
  constructor(private apiService: ApiService) {}
  ngOnInit(): void {
    this.apiService.checkAuth()
    .then((loggedIn) => {
      this.apiService.loginState.next(loggedIn);
    })
    .catch((error)=>{console.error(error);});
  }
}
