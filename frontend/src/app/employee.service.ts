import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Employee } from './Employee';

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {
  private baseUrl = 'http://localhost:8080';
  private getUrl = `${this.baseUrl}/list`;
  private createUrl = `${this.baseUrl}/create`;
  private deleteUrl =`${this.baseUrl}/delete`

  constructor(private http: HttpClient) { }

  getEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.getUrl);
  }

  addEmployee(employee: Employee): Observable<Employee> {
    return this.http.post<Employee>(this.createUrl, employee)
  }
  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.deleteUrl}/${id}`);
  }
}
