package com.pillar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillar.merchant.Merchant;
import com.pillar.merchant.MerchantRepository;
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


public class TestMerchantApiController {
    private static final String API_MERCHANT_1 = "/api/merchant/1";
    private static final String API_ALL_MERCHANTS = "/api/merchant";
    private static final String TEST_MERCHANT_NAME = "Test Merchant";
    private static final int TEST_MERCHANT_ID = 1;
    private static final String API_MERCHANT_2 = "/api/merchant/2";
    private static final String JSON_NAME_FIELD = "$.name";
    private static final String JSON_ID_FIELD = "$.id";
    private static final int INVALID_MERCHANT_ID = 0;

    private MerchantApiController controller;
    private MerchantRepository repository;
    private MockMvc mockMvc;
    private Merchant testMerchant;
    private List<Merchant> merchantRepoList;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        repository = mock(MerchantRepository.class);
        controller = new MerchantApiController(repository);

        testMerchant = new Merchant(TEST_MERCHANT_ID, TEST_MERCHANT_NAME);

        when(repository.findById(TEST_MERCHANT_ID)).thenReturn(Optional.of(testMerchant));

        merchantRepoList = new ArrayList<>(Collections.singletonList(testMerchant));
        when(repository.findAll()).then((invocation) -> merchantRepoList);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void merchantApiControllerExists() {
        assertNotNull(controller);
    }

    @Test
    public void merchantApiReturnsEmptyListIfNoMerchants() {
        merchantRepoList.clear();

        assertEquals(0, controller.getAll().size());
    }

    @Test
    public void merchantApiReturnsAListWithOneMerchantWhenTheRepositoryContainsOneMerchant() {
        assertEquals(1, controller.getAll().size());
    }

    @Test
    public void whenAMerchant1IsRequestedIGetTheTestMerchant() {
        ResponseEntity<Merchant> merchantResponse = controller.getMerchant(TEST_MERCHANT_ID);
        assertEquals(TEST_MERCHANT_ID, (int) extractIdFromMerchantResponse(merchantResponse));
    }

    @Test
    public void aGetRequestForMerchant1ReturnsStatusOK() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForMerchant1ReturnsAMerchantWithTheNameTestMerchant() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(jsonPath(JSON_NAME_FIELD, is(TEST_MERCHANT_NAME)));
    }

    @Test
    public void aGetRequestForMerchant1ReturnsAMerchantWithTheId1() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(jsonPath(JSON_ID_FIELD, is(TEST_MERCHANT_ID)));
    }

    @Test
    public void aGetRequestForMerchant2ReturnsStatusNotFound() throws Exception {
        queryApi(API_MERCHANT_2)
                .andExpect(status().isNotFound());
    }

    @Test
    public void aGetRequestForAllMerchantsReturnsStatusOk() throws Exception {
        queryApi(API_ALL_MERCHANTS)
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForAllMerchantsContainsTheTestMerchant() throws Exception {
        ResultActions result = queryApi(API_ALL_MERCHANTS);
        assertTrue(containsMerchantWithId(result, TEST_MERCHANT_ID));
    }

    private ResultActions queryApi(String s) throws Exception {
        return mockMvc.perform(get(s));
    }

    private Integer extractIdFromMerchantResponse(ResponseEntity<Merchant> merchantResponse) {
        return Optional.ofNullable(merchantResponse.getBody())
                .map(Merchant::getId)
                .orElse(INVALID_MERCHANT_ID);
    }

    private boolean containsMerchantWithId(ResultActions result, int testMerchantId) throws IOException {
        return getMerchantsFromResponseJson(result)
                .map(Merchant::getId)
                .anyMatch(id -> id == testMerchantId);
    }

    private Stream<Merchant> getMerchantsFromResponseJson(ResultActions result) throws IOException {
        String content = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Merchant> merchants = objectMapper.readValue(content, new TypeReference<List<Merchant>>() {
        });
        return merchants.stream();
    }
}
