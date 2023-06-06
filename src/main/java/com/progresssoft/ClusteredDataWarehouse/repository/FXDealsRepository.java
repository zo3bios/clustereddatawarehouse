package com.progresssoft.ClusteredDataWarehouse.repository;
import com.progresssoft.ClusteredDataWarehouse.model.FXDeals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FXDealsRepository extends JpaRepository<FXDeals, String> {

}
