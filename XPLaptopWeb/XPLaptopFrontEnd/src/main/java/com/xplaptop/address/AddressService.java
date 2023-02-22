package com.xplaptop.address;

import com.xplaptop.common.entity.country.Country;
import com.xplaptop.common.entity.country.State;
import com.xplaptop.common.entity.customer.Address;
import com.xplaptop.common.entity.customer.Customer;
import com.xplaptop.common.exception.AddressNotFoundException;
import com.xplaptop.setting.CountryRepository;
import com.xplaptop.setting.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private StateRepository stateRepository;

    public List<Address> getAllCustomerAddress(Integer customerId) {
        return addressRepository.findAllByCustomer_Id(customerId);
    }

    public List<Country> listAllCountry() {
        return countryRepository.findAllByOrderByNameAsc();
    }

    public List<State> listAllStateFromCountry(Integer countryId) {
        return stateRepository.findAllByCountry_Id(countryId);
    }

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public Address getAddressByIdAndCustomer(Integer addressId, Customer customer) {
        return addressRepository.findByIdAndCustomer_Id(addressId, customer.getId()).orElseThrow(
                () -> new AddressNotFoundException("Address not found")
        );
    }

    public void deleteAddressByIdAndCustomer(Integer addressId, Customer customer) {
        getAddressByIdAndCustomer(addressId, customer);
        addressRepository.deleteByIdAndCustomer_Id(addressId, customer.getId());
    }


    public void changeDefaultAddress(Integer addressIdToDefault, Customer customer) {
        if(addressIdToDefault == 0) {
            setDefaultAddressToFalseForAll(customer);
            return;
        }
        Address address = getAddressByIdAndCustomer(addressIdToDefault, customer);
        setDefaultAddressToFalseForAll(customer);
        address.setDefaultAddress(true);
        addressRepository.save(address);
    }

    public void setDefaultAddressToFalseForAll(Customer customer) {
        List<Address> allCustomerAddress = getAllCustomerAddress(customer.getId());
        allCustomerAddress.forEach(a -> a.setDefaultAddress(false));
        addressRepository.saveAll(allCustomerAddress);
    }

    public Address getDefaultAddress(Customer customer) {
        return addressRepository.findByDefaultAddressAndCustomer_Id(customer.getId()).orElseThrow(
                () -> new AddressNotFoundException("Address not found")
        );
    }

    public boolean usePrimaryAsDefaultAddress(Customer customer) {
        return addressRepository.findByDefaultAddressAndCustomer_Id(customer.getId()).isEmpty();
    }
}
