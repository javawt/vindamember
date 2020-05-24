package com.songlanyun.jymall.modules.business.jysys.address.repository;

import com.songlanyun.jymall.common.JpaUtils.BaseRepository;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:26
 */
@Repository
public interface AddressRepository extends BaseRepository<Address, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Address SET isDefault=0 WHERE userId= ?1")
    void updateDefaultAddressBack(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE Address SET isDefault=1 WHERE userId= ?1 AND addressId= ?2")
    void updateDefaultAddress(String userId, Integer addressId);

}
