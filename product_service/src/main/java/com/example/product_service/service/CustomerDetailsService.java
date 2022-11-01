package com.example.product_service.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.product_service.model.CustomerDetails;
import com.example.product_service.repository.CustomerDetailsRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerDetailsService {
    private final Logger log = LoggerFactory.getLogger(CustomerDetailsService.class);

    private final CustomerDetailsRepository customerDetailsRepository;

    /**
     * Create a new customer details 
     * @param customerDetails the entity to save 
     * @return the persisted data
     */
    public CustomerDetails createCustomerDetails(CustomerDetails customerDetails) { 
        log.debug("Request to save Customer Details: {}", customerDetails);
        return customerDetailsRepository.save(customerDetails);
    }
    
    /**
     * Gets all the customer details
     * @param pageable the pagination information 
     * @return return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CustomerDetails> getAll(Pageable pageable) { 
        log.debug("Request to get all Customer Details");
        return customerDetailsRepository.findAll(pageable);
    }

    /**
     * Delete the customer details by id 
     * @param id of the entity to delete
     */
    public void delete(final Long id) { 
        log.debug("Request to delete the customer details");

        customerDetailsRepository.deleteById(id);
    }

    /**
     * Gets a customer details by id 
     * @param id of the customer detail
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDetails> findById(final Long id) { 
        log.debug("Request to get customer details");

        return customerDetailsRepository.findById(id);
    }

    /**
     * Partially update a customer details 
     * 
     * @param customerDetails the entity to update partially 
     * @return the persisited entity
     */
    public Optional<CustomerDetails> partialUpdateDetails(CustomerDetails customerDetails) { 
        log.debug("Request to partially update Customer Details: {}", customerDetails);

        return customerDetailsRepository
            .findById(customerDetails.getId())
            .map(existingDetails -> {
                    if (customerDetails.getGender() != null) existingDetails.setGender(customerDetails.getGender());
                    if (customerDetails.getPhone() != null) existingDetails.setPhone(customerDetails.getPhone());
                    if (customerDetails.getAddressLine1() != null) existingDetails.setAddressLine1(customerDetails.getAddressLine1());
                    if (customerDetails.getAddressLine2() != null) existingDetails.setAddressLine2(customerDetails.getAddressLine2());
                    if (customerDetails.getCity() != null) existingDetails.setCity(customerDetails.getCity());
                    if (customerDetails.getCountry() != null) existingDetails.setCountry(customerDetails.getAddressLine1());
                    return existingDetails;
                }
            )
        .map(customerDetailsRepository::save);
    }
}
