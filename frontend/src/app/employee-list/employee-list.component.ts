import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Thêm để dùng *ngFor
import { EmployeeService } from '../employee.service';
import { Employee } from '../Employee';

@Component({
  selector: 'app-employee-list',
  standalone: true, // Đánh dấu là standalone
  imports: [CommonModule], // Thêm CommonModule để dùng *ngFor
  template: `
    <h2>Danh sách nhân viên</h2>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>Tên</th>
          <th>Tuổi</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let employee of employees">
          <td>{{ employee.id }}</td>
          <td>{{ employee.name }}</td>
          <td>{{ employee.age }}</td>
        </tr>
      </tbody>
    </table>
  `,
  styles: [`
    table {
      width: 100%;
      border-collapse: collapse;
    }
    th, td {
      border: 1px solid black;
      padding: 8px;
      text-align: left;
    }
  `]
})
export class EmployeeListComponent implements OnInit {
  employees: Employee[] = [];

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.refreshList(); 
  }

  refreshList(): void {
    this.employeeService.getEmployees().subscribe({
      next: (data) => {
        this.employees = data; 
      },
      error: (err) => {
        console.error('Lỗi khi tải danh sách:', err); 
      }
    });
  }
}