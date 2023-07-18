package com.ghtk.repository;

import com.ghtk.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query(
            value = "SELECT shop_id FROM staff WHERE id = ?1",
            nativeQuery = true
    )
    int findShopIdByStaffId(int staffId);


}
