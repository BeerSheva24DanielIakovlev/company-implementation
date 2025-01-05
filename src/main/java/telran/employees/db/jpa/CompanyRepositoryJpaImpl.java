package telran.employees.db.jpa;

import java.lang.reflect.Constructor;
import java.util.*;
import jakarta.persistence.*;
import jakarta.persistence.spi.PersistenceProvider;
import jakarta.persistence.spi.PersistenceUnitInfo;
import telran.employees.Employee;
import telran.employees.db.CompanyRepository;

public class CompanyRepositoryJpaImpl implements CompanyRepository {
    private EntityManager em;

    public CompanyRepositoryJpaImpl(PersistenceUnitInfo persistenceUnit,
    HashMap<String, Object> properties) {
        try {
            String providerName = persistenceUnit.getPersistenceUnitName();
            @SuppressWarnings("unchecked")
            Class<PersistenceProvider> clazz = (Class<PersistenceProvider>) Class.forName(providerName);
            Constructor<PersistenceProvider> constructor = clazz.getConstructor();
            PersistenceProvider provider = constructor.newInstance();
            EntityManagerFactory emf = provider.createContainerEntityManagerFactory(persistenceUnit, properties);
            em = emf.createEntityManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public List<Employee> getEmployees() {
        TypedQuery<EmployeeEntity> query = em.createQuery("select empl from EmployeeEntity empl", EmployeeEntity.class);
        return toEmployeeList(query.getResultList());
    }
        
    private List<Employee> toEmployeeList(List<EmployeeEntity> resultList) {
        return resultList.stream().map(EmployeesMapper::toEmployeeDtoFromEntity).toList();
    }

}
