package net.knowledgebase.springboot.web;

import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.repository.CompanyRepository;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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

    public SalesforceController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Scheduled(fixedDelay = 1000*60*5, initialDelay = 1000*10)
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

        final int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK) {
            System.out.println("Error authenticating to Force.com: " + statusCode);
            return;
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
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Account_Status__c") != "null") {
                            company.setStatus(json.getJSONArray("records").getJSONObject(i).getString("Account_Status__c"));
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Type") != "null") {
                            company.setType(json.getJSONArray("records").getJSONObject(i).getString("Type"));
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Type") != "null") {
                            company.setType(json.getJSONArray("records").getJSONObject(i).getString("Type"));
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("Website") != "null") {
                            company.setWebsite(json.getJSONArray("records").getJSONObject(i).getString("Website"));
                        }
                        if (json.getJSONArray("records").getJSONObject(i).getString("BillingAddress") != "null") {
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("street") != "null") {
                                company.setAddr1(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("street"));
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("city") != "null") {
                                company.setAddr2(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("city"));
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("state") != "null") {
                                company.setAddr4(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("state"));
                            }
                            if (json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("postalCode") != "null") {
                                company.setEircode(json.getJSONArray("records").getJSONObject(i).getJSONObject("BillingAddress").getString("postalCode"));
                            }
                        }
                        companyRepository.save(company);
                    }
                } catch (JSONException je) {
                    je.printStackTrace();
                }
            } else {
                System.exit(-1);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }
}
