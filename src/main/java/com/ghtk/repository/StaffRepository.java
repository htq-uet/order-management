package com.ghtk.repository;

import com.ghtk.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    @Query(
            value = "SELECT shop_id FROM staff WHERE id = ?1",
            nativeQuery = true
    )
    int findShopIdByStaffId(int staffId);


    @Query(
            value = "SELECT user_id FROM staff WHERE id = ?1",
            nativeQuery = true
    )
    Integer findUserIdById(int staffId);


    @Query(
            value = "SELECT staff.* FROM staff WHERE shop_id = ?1",
            nativeQuery = true
    )
    List<Staff> findAllByShopId(int shopId);


    @Query(
            nativeQuery = true,
            value = "SELECT * FROM staff s left join user u on s.user_id = u.id where u.username = ?1"
    )
    Staff findStaffByUsername(String username);
}
