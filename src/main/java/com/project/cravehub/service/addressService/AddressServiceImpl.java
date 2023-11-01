package com.project.cravehub.service.addressService;

import com.project.cravehub.model.user.Address;
import com.project.cravehub.repository.AddressRepository;
import com.project.cravehub.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public Address deleteAddressById(Integer addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);
        if(optionalAddress.isPresent()) {
            addressRepository.deleteById(addressId);
            return  optionalAddress.get();
        }
        else {
            return null;
        }
    }

    @Override
    public boolean isAddressInPurchaseOrder(Integer addressId) {
        Address address = addressRepository.findById(addressId).orElse(null);
        if(address != null) {
            return purchaseOrderRepository.existsByAddress(address);

        }
        return false;
    }
}
