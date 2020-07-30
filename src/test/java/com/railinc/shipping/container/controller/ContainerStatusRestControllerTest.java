package com.railinc.shipping.container.controller;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.railinc.shipping.container.model.ContainerStatus;
import com.railinc.shipping.container.service.ContainerStatusRestService;
import com.railinc.shipping.container.util.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(value = ContainerStatusRestController.class)
@WithMockUser
public class ContainerStatusRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContainerStatusRestService service;

    private ContainerStatus mockContainerStatus;

    private String json;

    private String expectedJson;


    @Before
    public void setUp() throws Exception {
        try {

            json = StreamUtils.copyToString(new ClassPathResource("containerStatus.json").getInputStream(), Charset.defaultCharset());
            expectedJson = json;
            mockContainerStatus = (ContainerStatus) JSONUtil.fromJSON(json, ContainerStatus.class);
            System.out.println("done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

    @Test
    public void testCreateContainer() throws Exception {

        Mockito.when(
                service.createContainer(Mockito.anyInt())).thenReturn(mockContainerStatus);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/shipping/container/1")
                .accept(MediaType.APPLICATION_JSON).content(json)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        int status = response.getStatus();
        String responseContent = response.getContentAsString();

        assertEquals(HttpStatus.OK.value(), status);
        JSONAssert.assertEquals(expectedJson, responseContent, false);

    }

    @Test
    public void testGetAllRniFulfillments() throws Exception {

        List<ContainerStatus> list = new ArrayList<ContainerStatus>();
        ContainerStatus c1 = mockContainerStatus;
        ContainerStatus c2 = mockContainerStatus;
        c2.setContainerId(2);
        ContainerStatus c3 = mockContainerStatus;
        c2.setContainerId(3);

        list.add(c1);
        list.add(c2);
        list.add(c3);

        Mockito.when(
                service.getContainersByOwner(Mockito.any(Integer.class)))
                .thenReturn(list);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/shipping/container/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        int status = response.getStatus();
        String content = response.getContentAsString();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        ContainerStatus[] containerStatuses = mapFromJson(content, ContainerStatus[].class);
        assertTrue(containerStatuses.length == 3);

    }

    @Test
    public void testUpdateContainer() throws Exception {

        mockContainerStatus.setStatus("WAITING_FOR_PICKUP");
        Mockito.when(
                service.updateContainer(Mockito.any(Integer.class),
                        Mockito.any(String.class))).thenReturn(mockContainerStatus);

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/shipping/container/1?status=WAITING_FOR_PICKUP")
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        String updateExpected = expectedJson.replace("\"status\": \"AVAILABLE\"", "\"status\": \"WAITING_FOR_PICKUP\"");
        int status = response.getStatus();
        String responseContent = response.getContentAsString();
        //System.out.println(s);
        //String header =response.getHeader(HttpHeaders.AUTHORIZATION);

        //        String expected = "{\"fulfillmentId\":\"2\",\"uuid\":\"1ba9a35a-f340-4f4d-95bb-dceb5b662be9\",\"rniIdentification\":{\"dbType\":\"MSSQL\",\"rniId\":6403,\"rniName\":\"self080\",\"domain\":\"davis.sensus.lab\",\"utilityName\":\"LF080\",\"utilityCodes\":[0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15],\"rniSize\":\"Small\",\"rniVersion\":\"4.4.1\",\"dbServer\":\"self080-db\",\"dbServerRegistry\":null,\"rniServer\":\"rni-mgmt\",\"rniInfraRegistry\":null},\"mssqlSettings\":{\"dbTablePath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_DATA\",\"mssqlDbSize\":\"64\",\"dbIncrement\":\"64\",\"logfilePath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_LOG\",\"logfileSize\":\"64\",\"logfileIncrement\":\"64\",\"matrixPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_MatrixFileGroup\",\"matrixSize\":\"64\",\"matrixIncrement\":\"64\",\"onairPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_OnAirFileGroup\",\"onairSize\":\"64\",\"onairIncrement\":\"64\",\"tempPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_Temp\",\"tempSize\":\"64\",\"tempIncrement\":\"64\",\"alarmsPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_Alarms\",\"alarmsSize\":\"64\",\"alarmsIncrement\":\"64\",\"dwPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_DW\",\"dwSize\":\"64\",\"dwIncrement\":\"64\",\"appLoggingPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_App_Logging\",\"appLoggingSize\":\"64\",\"appLoggingIncrement\":\"64\"},\"dbBuilderSettings\":{\"dbPartitioning\":\"No\",\"dbbUpgrade\":\"No\",\"deliveriesPath\":\"/var/opt/flexnet/data-extracts\",\"messagesRetentionDays\":\"60\",\"readsRetentionDays\":\"60\",\"alarmsRetentionDays\":\"60\",\"appLoggingRetentionDays\":\"60\",\"dwRetentionDays\":\"60\",\"dbSchema\":\"flexnetdb\"},\"flexnetDbSecurity\":{\"flexnetdbUser\":\"flexnetdb\",\"flexnetdbPassword\":\"Sensus1!!\",\"ustatsFlexnetdbUser\":\"ustats_flexnetdb\",\"ustatsFlexnetdbPassword\":\"Sensus1!\",\"opsFlexnetdbUser\":\"ops_flexnetdb\",\"opsFlexnetdbPassword\":\"Sensus1!\",\"epmFlexnetdbUser\":\"epm_flexnetdb\",\"epmFlexnetdbPassword\":\"Sensus1!\",\"gatewayFlexnetdbUser\":\"gateway_flexnetdb\",\"gatewayFlexnetdbPassword\":\"Sensus1!\",\"mdmifFlexnetdbUser\":null,\"mdmifFlexnetdbPassword\":\"Sensus1!\",\"parsersFlexnetdbUser\":null,\"parsersFlexnetdbPassword\":\"Sensus1!\",\"readsFlexnetdbUser\":null,\"readsFlexnetdbPassword\":\"Sensus1!\",\"reportsFlexnetdbUser\":null,\"reportsFlexnetdbPassword\":\"Sensus1!\",\"schedulerFlexnetdbUser\":null,\"schedulerFlexnetdbPassword\":\"Sensus1!\",\"sqltoolsFlexnetdbUser\":null,\"sqltoolsFlexnetdbPassword\":\"Sensus1!\",\"statsFlexnetdbUser\":null,\"statsFlexnetdbPassword\":\"Sensus1!\",\"twowayFlexnetdbUser\":null,\"twowayFlexnetdbPassword\":\"Sensus1!\",\"webFlexnetdbUser\":null,\"webFlexnetdbPassword\":\"Sensus1!\",\"nmsFlexnetdbUser\":null,\"nmsFlexnetdbPassword\":\"Sensus1!\",\"cpFlexnetdbUser\":null,\"cpFlexnetdbPassword\":\"Sensus1!\"},\"rniSecurity\":{\"storeType\":\"local\",\"ridp\":{\"baseUrl\":\"https://idp.company.com\",\"clientId\":\"flexnet-rni\",\"clientSecret\":\"shh-dont-look\",\"clientRealm\":\"TIS71\",\"clientRealmCustomized\":false},\"lldap\":{\"bindPassword\":\"gNSyP4fKDF0fukug\",\"managerPassword\":\"j6nvmTWrSa80i78O\",\"rootPassword\":\"WbhSTWyyZwMqUSkH\"},\"rldap\":{\"primaryUrl\":\"ldap://ral-rhds1.nc.flexnet.net:389\",\"secondaryUrl\":\"ldap://ral-rhds2.nc.flexnet.net:389\",\"topLevelDn\":\"o=customers,dc=flexnet,dc=net\",\"userBaseDn\":\"ou=people,o=3,o=customers,dc=flexnet,dc=net\",\"rolesBaseDn\":\"ou=groups,o=3,o=customers,dc=flexnet,dc=net\",\"bindUser\":\"cn=3_bind,ou=people,o=customers,dc=flexnet,dc=net\",\"bindPassword\":\"Sensus123\",\"managerUser\":\"cn=3_mgr,ou=people,o=customers,dc=flexnet,dc=net\",\"managerPassword\":\"Sensus123\",\"rdnModifier\":\"UTILITY_NAME\",\"location\":\"ral\",\"additionServerDetails\":false,\"serverTimeZone\":\"\",\"assignedNumber\":\"\"},\"ad\":{\"primaryUrl\":\"ldap://adserver1.company.com\",\"secondaryUrl\":\"ldap://adserver2.company.com\",\"userBaseDn\":\"CN=Users,DC=company,DC=com\",\"rolesBaseDn\":\"OU=Roles,OU=FlexNet,DC=company,DC=com\",\"bindUser\":\"binduser@company.com\",\"bindPassword\":\"AdSensus123\"},\"chosenStoreType\":null},\"cryptoSettings\":{\"useHsm\":false,\"hsmTokenLabel\":\"hsmtokenlabel\",\"hsmTokenId\":\"hsntokenid\",\"hsmVersion\":\"LUNA5\",\"allowKeyExtraction\":true,\"kdfIdentifier\":\"www.6403.self080.davis.sensus.lab\",\"kdfIdentifierCustomized\":false},\"miscSettings\":{\"opsEmail\":\"ops@sensus.com\",\"webUrl\":\"self080-combo.davis.sensus.lab\",\"webUrlCustomized\":false,\"activeMqPassword\":\"random\",\"windowsBaseDir\":\"C:\\\\Sensus\",\"shouldHarden\":true,\"systemSizeCustomized\":false,\"schedulerSystemJobsOnly\":false},\"postgresSettings\":null,\"status\":\"New\",\"rniTimeZone\":274,\"meterTimeZones\":[274],\"deploymentResult\":null,\"cryptoKey\":\"lQmrio0O0V6U3Il5\",\"createdTimeEpoch\":1568843728261,\"createdTime\":\"2019-09-18 21:55:28.261\",\"fulfilledTimeEpoch\":null,\"fulfilledTime\":null,\"log\":\"generate self080 install-config/setEnc.sh and Push to Repo https://moc-bb.lab.sensus.net/bitbucket/scm/flexrni/rni-git-server.git(generateConfigAndPushToRepo) : generated Install-config https://moc-bb.lab.sensus.net/bitbucket/scm/flexrni/rni-git-server.git messages status=OK message null \",\"standalone\":\"false\",\"hostInfos\":null,\"userSecurity\":{\"windowsUser\":\"Administrator\",\"windowsPassword\":\"Sensus123\",\"linuxUser\":\"root\",\"linuxPassword\":\"sensus123\",\"chefUser\":\"root\",\"chefPassword\":\"sensus123\"},\"mssqlMiscSettings\":{\"superUserPassword\":\"Sensus123\",\"jobPath\":\"C:\\\\Sensus\\\\LF080\\\\SQL_JobLog\",\"dbPort\":1433},\"oracleSettings\":[{\"tableGroup\":\"ALARMSDATA\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"ALARMSINDEX\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"GENERALDATA\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"GENERALINDEX\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"MSGSOTHERDATA\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"MSGSOTHERINDEX\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"ONAIRDATA\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"ONAIRINDEX\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"OPS\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"READSDATA\",\"tableSpace\":\"FLEXNETDB_TS\"},{\"tableGroup\":\"READSINDEX\",\"tableSpace\":\"FLEXNETDB_TS\"}],\"oracleMiscSettings\":{\"sid\":\"fndb\",\"serviceName\":\"fndb.db\",\"sysPassword\":\"Sensus1!\",\"home\":\"C:\\\\oracle\\\\product\\\\12.1.0\\\\client_12.1.0\",\"dbPort\":1521},\"agentHosts\":null,\"devInstall\":true,\"testInstall\":true,\"dcopsInstall\":true,\"upgrade\":true,\"appliedPatches\":[],\"loadingErrors\":false}";
        assertEquals(HttpStatus.OK.value(), response.getStatus());

        JSONAssert.assertEquals(updateExpected, responseContent, false);
    }

    @Test
    public void testDeleteContainer() throws Exception {


        Mockito.when(
                service.deleteContainer(Mockito.any(Integer.class))).thenReturn("container deleted successfully!");
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/shipping/container/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        int status = response.getStatus();
        String responseContent = response.getContentAsString();

        assertEquals(HttpStatus.OK.value(), response.getStatus());

        String expected = "container deleted successfully!";

        assertEquals(expected, responseContent);
    }
}