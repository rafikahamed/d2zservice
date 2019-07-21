package com.d2z.d2zservice.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.d2z.d2zservice.entity.CSTickets;

public interface CSTicketsRepository extends CrudRepository<CSTickets, Long>, JpaSpecificationExecutor<CSTickets>{

	@Query( nativeQuery = true, value="SELECT NEXT VALUE FOR CSSeqnum")
	Integer fetchNextSeq();
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where status = :status and "
			+ "trackingEventDateOccured between :fromDate and :toDate and userId in (:userId)") 
	List<CSTickets> fetchEnquiry(@Param("status") String status, @Param("fromDate") String fromDate, @Param("toDate") String toDate,
									@Param("userId") Integer[] userIds);
	
	@Query( nativeQuery = true, value="SELECT * FROM CSTickets where userId in (:userId) and status = 'closed' and trackingEventDateOccured >= getdate() -14") 
	List<CSTickets> fetchCompletedEnquiry(@Param("userId") Integer[] userIds);
	
//	public List<Employee> findByCriteria(String employeeName){
//        return employeeDAO.findAll(new Specification<Employee>() {
//            @Override
//            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                if(employeeName!=null) {
//                    predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("employeeName"), employeeName)));
//                }
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        });
//    }
}
