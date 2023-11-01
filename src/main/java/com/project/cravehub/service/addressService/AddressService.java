package com.project.cravehub.service.addressService;

import com.project.cravehub.model.user.Address;

public interface AddressService {

    Address deleteAddressById(Integer addressId);

    boolean isAddressInPurchaseOrder(Integer addressId);
}
