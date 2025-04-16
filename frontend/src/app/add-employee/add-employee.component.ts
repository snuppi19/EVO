import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Thêm để dùng ngModel
import { EmployeeService } from '../employee.service';
import { Employee } from '../Employee';

@Component({
  selector: 'app-add-employee',
  standalone: true, // Đánh dấu là standalone
  imports: [FormsModule], // Thêm FormsModule để dùng ngModel
  template: `
    <h2>Thêm nhân viên mới</h2>
    <form (ngSubmit)="addEmployee()">
      <div>
        <label>ID:</label>
        <input type="number" [(ngModel)]="employee.id" name="id" required>
      </div>
      <div>
        <label>Tên:</label>
        <input type="text" [(ngModel)]="employee.name" name="name" required>
      </div>
      <div>
        <label>Tuổi:</label>
        <input type="number" [(ngModel)]="employee.age" name="age" required>
      </div>
      <button type="submit">Thêm</button>
    </form>
  `,
  styles: [`
    div {
      margin-bottom: 10px;
    }
    input {
      margin-left: 10px;
    }
  `]
})
export class AddEmployeeComponent {
  employee: Employee = { id: 0, name: '', age: 0 };
  @Output() employeeAdded = new EventEmitter<void>();

  constructor(private employeeService: EmployeeService) { }

  addEmployee(): void {

    this.employeeService.addEmployee(this.employee).subscribe(() => {
      this.employee = { id: 0, name: '', age: 0 };
      this.employeeAdded.emit();
    });

  }
}