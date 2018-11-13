package com.pillar;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pillar.merchant.Merchant;
import com.pillar.merchant.MerchantRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TestMerchantApiController {
    private static final String API_MERCHANT_1 = "/api/merchant/1";
    private MerchantApiController controller;
    private MerchantRepository repository;
    private MockMvc mockMvc;
    private Merchant testMerchant;
    private ArrayList<Merchant> merchantRepoList;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        repository = mock(MerchantRepository.class);
        controller = new MerchantApiController(repository);

        testMerchant = new Merchant(1, "Test Merchant");

        when(repository.getOne(1)).thenReturn(testMerchant);
        when(repository.findById(1)).thenReturn(Optional.of(testMerchant));

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
        int id = Optional.ofNullable(controller.getMerchant(1).getBody())
                .map(Merchant::getId)
                .orElse(0);
        assertEquals(1, id);
    }

    @Test
    public void aGetRequestForMerchant1ReturnsStatusOK() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForMerchant1ReturnsAMerchantWithTheNameTestMerchant() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(jsonPath("$.name", is("Test Merchant")));
    }

    @Test
    public void aGetRequestForMerchant1ReturnsAMerchantWithTheId1() throws Exception {
        queryApi(API_MERCHANT_1)
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void aGetRequestForMerchant2ReturnsStatusNotFound() throws Exception {
        queryApi("/api/merchant/2")
                .andExpect(status().isNotFound());
    }

    @Test
    public void aGetRequestForAllMerchantsReturnsStatusOk() throws Exception {
        queryApi("/api/merchant")
                .andExpect(status().isOk());
    }

    @Test
    public void aGetRequestForAllMerchantsContainsTheTestMerchant() throws Exception {
        long count = getMerchantsFromResponseJson(queryApi("/api/merchant"))
                .map(Merchant::getId)
                .filter(id -> id == 1)
                .count();

        assertEquals(1, count);
    }

    private ResultActions queryApi(String s) throws Exception {
        return mockMvc.perform(get(s));
    }

    private Stream<Merchant> getMerchantsFromResponseJson(ResultActions result) throws java.io.IOException {
        String content = result
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Merchant> merchants = objectMapper.readValue(content, new TypeReference<List<Merchant>>() {});
        return merchants.stream();
    }
}
