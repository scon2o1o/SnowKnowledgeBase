package net.knowledgebase.springboot.web;

import net.bytebuddy.utility.RandomString;
import net.knowledgebase.springboot.model.Client;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.model.Settings;
import net.knowledgebase.springboot.model.User;
import net.knowledgebase.springboot.repository.ClientRepository;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.repository.SettingsRepository;
import net.knowledgebase.springboot.repository.UserRepository;
import net.knowledgebase.springboot.service.SettingsService;
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@EnableScheduling
public class SalesforceController {


    public String USERNAME = "";
    public String PASSWORD = "";
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
    private static SettingsService settingsService;
    private SettingsRepository settingsRepository;

    public SalesforceController(CompanyRepository companyRepository, ClientRepository clientRepository, UserRepository userRepository, UserService userService, SettingsService settingsService, SettingsRepository settingsRepository) {
        this.companyRepository = companyRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.settingsService = settingsService;
        this.settingsRepository = settingsRepository;

        List<Settings> settings = settingsRepository.findAll();
        USERNAME = settings.get(0).getSfuser();
        PASSWORD = settings.get(0).getSfpass();
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
                        company.setName(json.getJSONArray("records").getJSONObject(i).getString("Name"));
                        company.setStatus(json.getJSONArray("records").getJSONObject(i).getString("Account_Status__c"));
                        company.setType(json.getJSONArray("records").getJSONObject(i).getString("Type"));
                        company.setWebsite(json.getJSONArray("records").getJSONObject(i).getString("Website"));
                        if (json.getJSONArray("records").getJSONObject(i).getString("BillingAddress") != "null") {
                            company.setAddr1(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("street"));
                            company.setAddr2(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("city"));
                            company.setAddr4(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("state"));
                            company.setEircode(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("postalCode"));
                        }
                        if (company.getAddr1() == null || Objects.equals(company.getAddr1(), "null")) {
                            company.setAddr1("");
                        }
                        if (company.getAddr2() == null || Objects.equals(company.getAddr2(), "null")) {
                            company.setAddr2("");
                        }
                        if (company.getAddr3() == null || Objects.equals(company.getAddr3(), "null")) {
                            company.setAddr3("");
                        }
                        if (company.getAddr4() == null || Objects.equals(company.getAddr4(), "null")) {
                            company.setAddr4("");
                        }
                        if (company.getEircode() == null || Objects.equals(company.getEircode(), "null")) {
                            company.setEircode("");
                        }
                        if (company.getEmail() == null || Objects.equals(company.getEmail(), "null")) {
                            company.setEmail("");
                        }
                        if (company.getName() == null || Objects.equals(company.getName(), "null")) {
                            company.setName("");
                        }
                        if (company.getPhone() == null || Objects.equals(company.getPhone(), "null")) {
                            company.setPhone("");
                        }
                        if (company.getStatus() == null || Objects.equals(company.getStatus(), "null")) {
                            company.setStatus("");
                        }
                        if (company.getType() == null || Objects.equals(company.getType(), "null")) {
                            company.setType("");
                        }
                        if (company.getWebsite() == null || Objects.equals(company.getWebsite(), "null")) {
                            company.setWebsite("");
                        }
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
                        client.setCompany(companyRepository.findCompanyNameByID(json.getJSONArray("records").getJSONObject(i).getString("AccountId")));
                        client.setEmail(json.getJSONArray("records").getJSONObject(i).getString("Email"));
                        client.setFirstName(json.getJSONArray("records").getJSONObject(i).getString("FirstName"));
                        client.setPhone(json.getJSONArray("records").getJSONObject(i).getString("Phone"));
                        client.setLastName(json.getJSONArray("records").getJSONObject(i).getString("LastName"));
                        client.setMobile(json.getJSONArray("records").getJSONObject(i).getString("MobilePhone"));
                        if (json.getJSONArray("records").getJSONObject(i).getString("Email") != "null" || client.getEmail() != null) {
                            //client.setAccount(true);
                            if (client.isAccount()) {
                                User user = userRepository.findByEmail(client.getEmail());
                                if (user == null) {
                                    UserRegistrationDto registrationDto = new UserRegistrationDto(client.getFirstName(), client.getLastName(), client.getEmail(), null, "Client", RandomString.make(30));
                                    userService.save(registrationDto);
                                }
                            }
                        }
                        Boolean suspended = false;
                        Optional<Client> exitingClient = clientRepository.findById(json.getJSONArray("records").getJSONObject(i).getString("Id"));
                        try{
                            if (exitingClient != null){
                                suspended = exitingClient.get().isSuspended();
                            }
                            client.setSuspended(suspended);
                        } catch(Exception e){
                            client.setSuspended(suspended);
                        }
                        if (client.getCompany() == null || Objects.equals(client.getCompany(), "null")) {
                            client.setCompany("");
                        }
                        if (client.getEmail() == null || Objects.equals(client.getEmail(), "null")) {
                            client.setEmail("");
                        }
                        if (client.getFirstName() == null || Objects.equals(client.getFirstName(), "null")) {
                            client.setFirstName("");
                        }
                        if (client.getLastName() == null || Objects.equals(client.getLastName(), "null")) {
                            client.setLastName("");
                        }
                        if (client.getMobile() == null || Objects.equals(client.getMobile(), "null")) {
                            client.setMobile("");
                        }
                        if (client.getPhone() == null || Objects.equals(client.getPhone(), "null")) {
                            client.setPhone("");
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
