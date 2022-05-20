package net.knowledgebase.springboot.web;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.UserService;
import net.knowledgebase.springboot.web.dto.UserRegistrationDto;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@EnableScheduling
public class SalesforceController {
    public final String USERNAME = "shane.concannon@snowtechnology.ie";
    public final String PASSWORD = "P@$$w0rd12bD5MDFFm7tJqKaX6ZYXbil4I3";
    public final String LOGINURL = "https://quantumsoftware2021.my.salesforce.com";
    public final String GRANTSERVICE = "/services/oauth2/token?grant_type=password";
    public final String CLIENTID = "3MVG9SOw8KERNN090EAFBk3D6yhJUqlc.qMMF0oztgCt9oMb3miy4_NUACgAlEljctNQNYfQxjNRCuA_N2AFH";
    public final String CLIENTSECRET = "0C7D5CAE0F4E58172C0C0A192873C315CEAC28325CC2D89C0F1C07F92C19C57A";
    public final String REST_ENDPOINT = "/services/data";
    public final String API_VERSION = "/v53.0";
    public static String baseUri;
    public static Header oauthHeader;
    public static Header prettyPrintHeader = new BasicHeader("X-PrettyPrint", "1");

    private static CompanyRepository companyRepository;
    private static ClientRepository clientRepository;
    private static UserRepository userRepository;
    private static UserService userService;

    public SalesforceController(CompanyRepository companyRepository, ClientRepository clientRepository, UserRepository userRepository, UserService userService) {
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Scheduled(fixedDelay = 1000 * 60, initialDelay = 1000 * 10)
    public void salesforceTest() {

        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        String loginURL = LOGINURL +
                GRANTSERVICE +
                "&client_id=" + CLIENTID +
                "&client_secret=" + CLIENTSECRET +
                "&username=" + USERNAME +
                "&password=" + PASSWORD;

        HttpPost httpPost = new HttpPost(loginURL);
        HttpResponse response = null;

        try {
            response = httpclient.execute(httpPost);
        } catch (ClientProtocolException cpException) {
            cpException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        String getResult = null;
        try {
            getResult = EntityUtils.toString(response.getEntity());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        JSONObject jsonObject = null;
        String loginAccessToken = null;
        String loginInstanceUrl = null;

        try {
            jsonObject = (JSONObject) new JSONTokener(getResult).nextValue();
            loginAccessToken = jsonObject.getString("access_token");
            loginInstanceUrl = jsonObject.getString("instance_url");
        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }

        baseUri = loginInstanceUrl + REST_ENDPOINT + API_VERSION;
        oauthHeader = new BasicHeader("Authorization", "OAuth " + loginAccessToken);
        queryAccounts();
        queryClients();
        httpPost.releaseConnection();
    }

    public static void queryAccounts() {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            String uri = baseUri + "/queryAll?q=SELECT+id,name,type,website,Account_Status__c,BillingAddress+from+Account";
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                try {
                    JSONObject json = new JSONObject(response_string);
                    JSONArray j = json.getJSONArray("records");
                    for (int i = 0; i < j.length(); i++) {
                        Company company = new Company();
                        company.setId(json.getJSONArray("records").getJSONObject(i).getString("Id"));
                        if (json.getJSONArray("records").getJSONObject(i).getString("Name") != "null") {
                            company.setName(json.getJSONArray("records").getJSONObject(i).getString("Name"));
                        } else{
                            company.setName("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Account_Status__c") != "null") {
                            company.setStatus(json.getJSONArray("records").getJSONObject(i).getString("Account_Status__c"));
                        } else {
                            company.setStatus("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Type") != "null") {
                            company.setType(json.getJSONArray("records").getJSONObject(i).getString("Type"));
                        } else {
                            company.setType("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Website") != "null") {
                            company.setWebsite(json.getJSONArray("records").getJSONObject(i).getString("Website"));
                        } else{
                            company.setWebsite("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("BillingAddress") != "null") {
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("street") != "null") {
                                company.setAddr1(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("street"));
                            } else {
                                company.setAddr1("");
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("city") != "null") {
                                company.setAddr2(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("city"));
                            } else {
                                company.setAddr2("");
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("state") != "null") {
                                company.setAddr4(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("state"));
                            } else {
                                company.setAddr4("");
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("postalCode") != "null") {
                                company.setEircode(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("postalCode"));
                            } else {
                                company.setEircode("");
                            }
                        }
                        company.setAddr3("");
                        company.setPhone("");
                        company.setEmail("");
                        companyRepository.save(company);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public static void queryClients() {
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();

            String uri = baseUri + "/queryAll?q=SELECT+id,FirstName,LastName,AccountId,Email,Phone,MobilePhone+FROM+Contact";
            HttpGet httpGet = new HttpGet(uri);
            httpGet.addHeader(oauthHeader);
            httpGet.addHeader(prettyPrintHeader);

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                String response_string = EntityUtils.toString(response.getEntity());
                try {
                    JSONObject json = new JSONObject(response_string);
                    JSONArray j = json.getJSONArray("records");
                    for (int i = 0; i < j.length(); i++) {
                        Client client = new Client();
                        client.setId(json.getJSONArray("records").getJSONObject(i).getString("Id"));
                        if (json.getJSONArray("records").getJSONObject(i).getString("AccountId") != "null") {
                            client.setCompany(companyRepository.findCompanyNameByID(json.getJSONArray("records").getJSONObject(i).getString("AccountId")));
                        } else {
                            client.setCompany("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Email") != "null") {
                            client.setEmail(json.getJSONArray("records").getJSONObject(i).getString("Email"));
                        } else {
                            client.setEmail("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("FirstName") != "null") {
                            client.setFirstName(json.getJSONArray("records").getJSONObject(i).getString("FirstName"));
                        } else {
                            client.setFirstName("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Phone") != "null") {
                            client.setPhone(json.getJSONArray("records").getJSONObject(i).getString("Phone"));
                        } else {
                            client.setPhone("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("LastName") != "null") {
                            client.setLastName(json.getJSONArray("records").getJSONObject(i).getString("LastName"));
                        } else {
                            client.setLastName("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("MobilePhone") != "null") {
                            client.setMobile(json.getJSONArray("records").getJSONObject(i).getString("MobilePhone"));
                        } else{
                            client.setMobile("");
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Email") != "null") {
                            //client.setAccount(true);
                            if (client.isAccount()) {
                                User user = userRepository.findByEmail(client.getEmail());
                                if (user == null) {
                                    UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(), client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                                    userService.save(registrationDto);
                                }
                            }
                        }
                        clientRepository.save(client);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
}
