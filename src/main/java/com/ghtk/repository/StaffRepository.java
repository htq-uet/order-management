package com.ghtk.repository;

import com.ghtk.model.DTO.StaffDTO;
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
            value = "SELECT " +
                    "s.id, s.name, s.phone, u.username, s.status " +
                    "FROM staff s " +
                    "left join user u " +
                    "on s.user_id = u.id " +
                    "WHERE s.shop_id = ?1",
            nativeQuery = true
    )
    List<StaffDTO> findAllByShopId(int shopId);


    @Query(
            nativeQuery = true,
            value = "SELECT s.* " +
                    "FROM staff s left join user u on s.user_id = u.id where u.username = ?1"
    )
    Staff findStaffByUsername(String username);

    @Query(
            nativeQuery = true,
            value = "SELECT s.status FROM staff s left join user u on s.user_id = u.id where u.username = ?1"
    )
    String getStatusByUsername(String username);


    @Query(
            nativeQuery = true,
            value = "SELECT s.* FROM staff s where s.id= ?1"
    )
    Staff findStaffById(int staffId);
}
