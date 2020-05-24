package com.songlanyun.jymall.modules.business.jysys.address.service.impl;

import com.songlanyun.jymall.common.JpaUtils.BaseServiceImpl;
import com.songlanyun.jymall.modules.business.jysys.address.entity.Address;
import com.songlanyun.jymall.modules.business.jysys.address.repository.AddressRepository;
import com.songlanyun.jymall.modules.business.jysys.address.service.AddressService;
import org.springframework.stereotype.Service;

/**
 * Created by zenghang.
 * Date: 2019/12/2
 * Time: 14:27
 */
@Service
public class AddressServiceImpl extends BaseServiceImpl<Address, Integer, AddressRepository> implements AddressService {
    public AddressServiceImpl(AddressRepository repository) {
        super(repository);
    }

    @Override
    public void updateDefaultAddress(Address address) {
        this.repository.updateDefaultAddressBack(address.getUserId());
        this.repository.updateDefaultAddress(address.getUserId(),address.getAddressId());
    }

    @Override
    public void updateDefaultAddressBack(Address address) {
        this.repository.updateDefaultAddressBack(address.getUserId());
    }
}
