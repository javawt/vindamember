package com.songlanyun.jymall.modules.business.jysys.address.service;

import com.songlanyun.jymall.common.JpaUtils.BaseService;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:27
 */
public interface AddressService extends BaseService<Address, Integer> {

    void updateDefaultAddress(Address address);

    void updateDefaultAddressBack(Address address);
}
