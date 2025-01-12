package telran.employees.db;

import java.util.Iterator;
import java.util.List;

import telran.employees.*;

public class CompanyDbImpl implements Company{
    private CompanyRepository repository;
    
    public CompanyDbImpl(CompanyRepository repository) {
        this.repository = repository;
    }

    private class CompanyImplIterator implements Iterator<Employee> {
        private Iterator<Employee> iterator = repository.getEmployees().iterator();
        private Employee prev = null;
    
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    
        @Override
        public Employee next() {
            return prev = iterator.next();
        }
    
        @Override
        public void remove() {
            if (prev == null) {
                throw new IllegalStateException();
            }
            removeEmployee(prev.getId());
            prev = null;
        }
    }
    

    @Override
    public Iterator<Employee> iterator() {
        return new CompanyImplIterator();
    }

    @Override
    public void addEmployee(Employee empl) {
        repository.insertEmployee(empl);
    }

    @Override
    public Employee getEmployee(long id) {
        return repository.findEmployee(id);
    }

    @Override
    public Employee removeEmployee(long id) {
        return repository.removeEmployee(id);
    }

    @Override
    public int getDepartmentBudget(String department) {
        List<Employee> employees = repository.getEmployeesByDepartment(department);
        return employees.stream().mapToInt(Employee::computeSalary).sum();
    }

    @Override
    public String[] getDepartments() {
       List<String> listDepartments = repository.findDepartments();
       return listDepartments.toArray(String[]::new);
    }

    @Override
    public Manager[] getManagersWithMostFactor() {
        List<Manager> managersList = repository.findManagersWithMaxFactor();
        return managersList.toArray(Manager[]::new); 
    }

}