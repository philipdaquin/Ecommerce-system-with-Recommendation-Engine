package com.example.product_service.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.product_service.model.CustomerDetails;
import com.example.product_service.repository.CustomerDetailsRepository;
import com.example.product_service.service.CustomerDetailsService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CustomerDetailResource {
    
    private Logger log = LoggerFactory.getLogger(CustomerDetailResource.class);
    private CustomerDetailsRepository customerDetailsRepository;
    private CustomerDetailsService customerDetailsService;
    private static final String ENTITY = "customer-details-category";

    /**
     * {@code POST /customer-details} : Create a new customerDetails
     * 
     * @param customerDetails the customerDetails to create 
     * @return the {@link ResponseEntity} with status {@code 201 (CREATED)} and with body 
     *      the new customerDetails, or with status {@code 400 (BAD REQUEST)} if the customer details 
     * @throws URISyntaxException if the Location URI syntax is incorrect 
     */
    @PostMapping("/customer-details")
    public ResponseEntity<CustomerDetails> createCustomerDetail(@Valid @RequestBody CustomerDetails customerDetails) throws URISyntaxException {
        log.debug("Request to create customer details: {}", customerDetails);

        if (customerDetails.getId() != null) throw new URISyntaxException("", "");
        
        CustomerDetails result = customerDetailsService.createCustomerDetails(customerDetails);
        return ResponseEntity
            .created(new URI("/api/customer-details/" + result.getId()))
            .header(ENTITY, result.getId().toString())
            .body(result);
    }

    /**
     * {@code GET /customer-details/:id} : get the "id" customerdetails 
     * 
     * @param id the id of the customer detailst to get 
     * @return the {@link ResponseEntity} with status {@code 200(OK)} and with body the customer details, or with status {@code 404 (NOT FOUND)}
     * @throws URISyntaxException
     */
    @GetMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetails> getCustomerDetails(@PathVariable Long id) throws URISyntaxException {
        log.debug("Request to get customer details of {}", id);
        Optional<CustomerDetails> result = customerDetailsService.findById(id);
        
        if (!result.isPresent()) throw new URISyntaxException("", "");
        return ResponseEntity
            .ok()
            .header(ENTITY, id.toString())
            .body(result.get());
    }
    /**
     * {@code DELETE /customer-details/:id } : Deletes the customer details by "id" 
     *  
     * @param id the id of the customerDetails to retrieve 
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} with body the customerDetails, or with status {@code 404 (NOT FOUND)}
     * @throws URISyntaxException
     */
    @DeleteMapping("/customer-details/{id}")
    public ResponseEntity<Void> deleteCustomerDetails(@PathVariable Long id) throws URISyntaxException { 
        log.debug("Request to delete customer details: {}", id);
        customerDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .header(ENTITY, id.toString())
            .build();
    }

    /**
     * Gets all the customer details by pagination
     * @param pageable the pagination informaiton 
     * @return the {@link ResponseEntity} with status {@code 202 (OK)} and the list of customerdetails in body 
     */
    @GetMapping("/customer-details/")
    public ResponseEntity<List<CustomerDetails>> getAllCustomerDetails(Pageable pageable)  { 
        log.debug("Request to get a page of Customer Details");
        Page<CustomerDetails> page = customerDetailsService.getAll(pageable);

        return ResponseEntity
            .ok()
            .header(ENTITY)
            .body(page.getContent());
    }

    /**
     * @{code PATCH /customer-details/:id} : Partial updates given fields of an existing customerdetails, field will 
     * ignore any null values 
     * 
     * @param id of the customerDetails to save
     * @param customerDetails the customerDetails to save 
     * @return
     * 
     * {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDetails 
     * 
     * 
     * @throws URISyntaxException if the Location URI syntax is incorrect 
     */
    @PatchMapping(value = "/customer-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<CustomerDetails> partialUpdateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerDetails customerDetails
    ) throws URISyntaxException { 
        log.debug("Request to partially update values in CustomerDetails: {}, {}", id, customerDetails);
        
        if (customerDetails.getId() == null) throw new URISyntaxException("", "");
        if (!Objects.equals(id, customerDetails.getId())) throw new URISyntaxException("", "");
        if (!customerDetailsRepository.existsById(id)) throw new URISyntaxException("", "");
        
        Optional<CustomerDetails> result = customerDetailsService.partialUpdateDetails(customerDetails);
        return ResponseEntity
            .ok()
            .header(ENTITY, id.toString())
            .body(result.get());
    }

    /**
     * @{code PUT /customer-details/:id} : updates an exiting customerDetails
     * 
     * @param id the id of the CustomerDetails to save
     * @param customerDetails the customerDetails to update
     * @return 
     * @throws URISyntaxException if the location URI syntax is incorrect
     */
    @PutMapping("/customer-details/{id}")
    public ResponseEntity<CustomerDetails> updateCustomerDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerDetails customerDetails
    ) throws URISyntaxException {
        log.debug("Request to update Customer Details: {}", customerDetails);
 
        if (customerDetails.getId() == null) throw new URISyntaxException("", "");
        if (!Objects.equals(id, customerDetails.getId())) throw new URISyntaxException("", "");
        if (!customerDetailsRepository.existsById(id)) throw new URISyntaxException("", "");

        CustomerDetails result = customerDetailsService.createCustomerDetails(customerDetails);
        return ResponseEntity
            .ok()
            .header(ENTITY, result.getId().toString())
            .body(result);

    }

}
