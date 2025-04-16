import { Component, ViewChild, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { AddEmployeeComponent } from './add-employee/add-employee.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    EmployeeListComponent,
    AddEmployeeComponent
  ],
  template: `
    <div>
      <h1>Quản lý nhân viên</h1>
      <nav>
        <a href="/employee-list">Danh sách</a> |
        <a href="/add-employee">Thêm mới</a>
      </nav>
      <app-employee-list #employeeList></app-employee-list>
      <app-add-employee (employeeAdded)="onEmployeeAdded()"></app-add-employee>
    </div>
  `,
  styleUrls: ['./app.component.css']
})
export class AppComponent implements AfterViewInit {
  title = 'employee-frontend';
  @ViewChild('employeeList') employeeListComponent!: EmployeeListComponent;
  private isViewInitialized = false;

  ngAfterViewInit(): void {
    this.isViewInitialized = true; // Đánh dấu view đã được khởi tạo
    console.log('ngAfterViewInit: employeeListComponent:', this.employeeListComponent); // Debug
  }

  onEmployeeAdded(): void {
    console.log('Nhận được sự kiện employeeAdded từ AddEmployeeComponent');
    if (this.isViewInitialized && this.employeeListComponent) {
      this.employeeListComponent.refreshList();
    } else {
      console.warn('employeeListComponent chưa được khởi tạo!');
    }
  }
}