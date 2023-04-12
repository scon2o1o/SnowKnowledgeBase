package net.knowledgebase.springboot.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.knowledgebase.springboot.model.Company;
import net.knowledgebase.springboot.repository.CompanyRepository;
import net.knowledgebase.springboot.service.SettingsService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Controller
public class PayslipController {
    private CompanyRepository companyRepository;
    private SettingsService settingsService;

    public PayslipController(CompanyRepository companyRepository, SettingsService settingsService) {
        super();
        this.companyRepository = companyRepository;
        this.settingsService = settingsService;
    }

    @GetMapping("/payslips")
    public String home(Model model, Model settingsModel) throws IOException, JSONException {
        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();
        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }

        List<Company> companyList = companyRepository.findByPortalIsNotNull();
        String data = "";
        String amendedData = "";
        for (int i = 0; i < companyList.size(); i++) {
            if (i == 0) {
                JSONArray array = new JSONArray(getData(companyList.get(i).getPortal() + "api/v1/payslips/report", companyList.get(i).getToken()));
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    object.put("Name", companyList.get(i).getName());
                    object.put("portal", companyList.get(i).getPortal());
                }
                amendedData = array.toString().substring(1, array.toString().length() - 1);
                data = data + amendedData;
            } else {
                JSONArray array = new JSONArray(getData(companyList.get(i).getPortal() + "api/v1/payslips/report", companyList.get(i).getToken()));
                for (int j = 0; j < array.length(); j++) {
                    JSONObject object = array.getJSONObject(j);
                    object.put("Name", companyList.get(i).getName());
                    object.put("portal", companyList.get(i).getPortal());
                }
                amendedData = array.toString().substring(1, array.toString().length() - 1);
                data = data + "," + amendedData;
            }
        }
        data = "[" + data + "]";
        model.addAttribute("jsonData", data);
        return "portal_report";
    }

    public String getData(String urlString, String bearerToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearerToken);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String returnData = response.toString();

        return returnData;
    }

    @GetMapping("/payslips/v2")
    public String getPayslipData(Model model, Model settingsModel) throws IOException, JSONException {
        List<Company> companyList = companyRepository.findByPortalIsNotNull();
        ArrayNode jsonArrayNode = JsonNodeFactory.instance.arrayNode();
        for (Company company : companyList) {
            JSONObject object = new JSONObject(getDatav2(company.getPortal() + "api/v1/payslips", company.getToken()));
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(object.toString());
            JsonNode dataNode = jsonNode.get("data");

            if (dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
                    objectNode.put("period", node.get("period").asText());
                    objectNode.put("created_at", node.get("created_at").asText());
                    objectNode.put("payroll", node.get("payroll").asText());
                    objectNode.put("Name", company.getName());
                    objectNode.put("portal", company.getPortal());
                    objectNode.put("payslip_count", 1);
                    jsonArrayNode.add(objectNode);
                }
            }
        }

        settingsModel.addAttribute("settings", settingsService.getAllSettings());
        List settings = settingsService.getAllSettings();

        if (settings.isEmpty()) {
            settingsModel.addAttribute("response", "NoData");
        } else {
            settingsModel.addAttribute("response", "");
        }

        model.addAttribute("jsonData", jsonArrayNode.toString());
        return "portal_report_v2";
    }


    public String getDatav2(String urlString, String bearerToken) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestMethod("GET");
        con.setRequestProperty("Authorization", "Bearer " + bearerToken);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        String returnData = response.toString();

        return returnData;
    }
}


