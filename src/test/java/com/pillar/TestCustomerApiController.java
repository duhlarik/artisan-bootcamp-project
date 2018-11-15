package com.pillar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillar.customer.Customer;
import com.pillar.customer.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestCustomerApiController {
    private static final String API_CUSTOMER_1 = "/api/customer/1";
    private static final String API_ALL_CUSTOMERS = "/api/customer";
    private static final String TEST_CUSTOMER_NAME = "Test Customer";
    private static final int TEST_CUSTOMER_ID = 1;
    private static final String API_CUSTOMER_2 = "/api/customer/2";
    private static final String JSON_NAME_FIELD = "$.name";
    private static final String JSON_ID_FIELD = "$.id";
    private static final int INVALID_CUSTOMER_ID = 0;

    private CustomerApiController controller;
    private CustomerRepository repository;
    private MockMvc mockMvc;
    private Customer testCustomer;
    private List<Customer> customerRepoList;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        repository = mock(CustomerRepository.class);
        controller = new CustomerApiController(repository);

        testCustomer = new Customer(TEST_CUSTOMER_ID, TEST_CUSTOMER_NAME);

        when(repository.findById(TEST_CUSTOMER_ID)).thenReturn(Optional.of(testCustomer));

        customerRepoList = new ArrayList<>(Collections.singletonList(testCustomer));
        when(repository.findAll()).then((invocation) -> customerRepoList);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void customerApiControllerExists() {
        assertNotNull(controller);
    }

    @Test
    public void customerApiReturnsEmptyListIfNoCustomer() {
        customerRepoList.clear();

        assertEquals(0, controller.getAll().size());
    }

    @Test
    public void customerApiReturnsAListWithOneCustomerWhenTheRepositoryContainsOneCustomer() {
        assertEquals(1, controller.getAll().size());
    }

    @Test
    public void whenACustomer1IsRequestedIGetTheTestCustomer() {
        ResponseEntity<Customer> customerResponse = controller.getCustomer(TEST_CUSTOMER_ID);
        assertEquals(TEST_CUSTOMER_ID, (int) extractIdFromCustomerResponse(customerResponse));
    }

    @Test
    public void aGetRequestForCustomer1ReturnsStatusOK() throws Exception {
        queryApi(API_CUSTOMER_1)
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForCustomer1ReturnsACustomerWithTheNameTestCustomer() throws Exception {
        queryApi(API_CUSTOMER_1)
                .andExpect(jsonPath(JSON_NAME_FIELD, is(TEST_CUSTOMER_NAME)));
    }

    @Test
    public void aGetRequestForCustomer1ReturnsACustomerWithTheId1() throws Exception {
        queryApi(API_CUSTOMER_1)
                .andExpect(jsonPath(JSON_ID_FIELD, is(TEST_CUSTOMER_ID)));
    }

    @Test
    public void aGetRequestForCustomer2ReturnsStatusNotFound() throws Exception {
        queryApi(API_CUSTOMER_2)
                .andExpect(status().isNotFound());
    }

    @Test
    public void aGetRequestForAllCustomersReturnsStatusOk() throws Exception {
        queryApi(API_ALL_CUSTOMERS)
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForAllCustomersContainsTheTestCustomer() throws Exception {
        ResultActions result = queryApi(API_ALL_CUSTOMERS);
        assertTrue(containsCustomerWithId(result, TEST_CUSTOMER_ID));
    }

    private ResultActions queryApi(String s) throws Exception {
        return mockMvc.perform(get(s));
    }

    private Integer extractIdFromCustomerResponse(ResponseEntity<Customer> customerResponse) {
        return Optional.ofNullable(customerResponse.getBody())
                .map(Customer::getId)
                .orElse(INVALID_CUSTOMER_ID);
    }

    private boolean containsCustomerWithId(ResultActions result, int testCustomerId) throws IOException {
        return getCustomersFromResponseJson(result)
                .map(Customer::getId)
                .anyMatch(id -> id == testCustomerId);
    }

    private Stream<Customer> getCustomersFromResponseJson(ResultActions result) throws IOException {
        String content = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Customer> customers = objectMapper.readValue(content, new TypeReference<List<Customer>>() {
        });
        return customers.stream();
    }
}
