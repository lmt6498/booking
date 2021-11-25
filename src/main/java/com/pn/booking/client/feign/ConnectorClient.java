package com.pn.booking.client.feign;

import java.util.List;

import com.pn.booking.client.feign.config.CognitoClientConfig;
import com.pn.booking.model.dto.RestResult;
import com.pn.booking.model.dto.response.ApplicationResponse;
import com.pn.booking.model.dto.response.MewsCredentialResponse;
import com.pn.booking.model.dto.response.RestPageResponse;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${service.connector.name}", url = "${service.connector.url}", configuration = CognitoClientConfig.class)
public interface ConnectorClient {

  @GetMapping("/integrations/mews/customers")
  ResponseEntity<RestResult<MewsCredentialResponse>> getMewsCustomer(
      @RequestParam(required = false) String enterpriseId,
      @RequestParam(required = false) Long businessId
  );

  @PostMapping("/integrations/mews/customers")
  ResponseEntity<RestResult<Object>> syncMewsIntegrationMessage(@RequestBody Object request);

  @GetMapping("/applications/is-activated")
  ResponseEntity<RestResult<Boolean>> isActivated(
      @RequestParam(required = true) Long businessId,
      @RequestParam(required = true) String businessType,
      @RequestParam(required = true) String clientId
  );

  @GetMapping("/integrations/cloudbeds/customers/integrate")
  ResponseEntity<RestResult<MewsCredentialResponse>> getCloudbedsIntegrationData(
          @RequestParam("businessId") Long businessId
  );

  @GetMapping("/integrations/cloudbeds/customers/webhook/integrate")
  ResponseEntity<RestResult<MewsCredentialResponse>> getCloudbedsCredentialWebhook(
          @RequestParam("enterpriseId") String enterpriseId
  );

  @PostMapping("integrations/cloudbeds/customers/refresh")
  ResponseEntity<RestResult<Boolean>> refresh(@RequestParam String refreshToken);

  @GetMapping("/applications/webhooks")
  ResponseEntity<RestResult<List<String>>> getWebhookUrls(
      @RequestParam(required = true) Long businessId,
      @RequestParam(required = true) String businessType,
      @RequestParam(required = true) String event
  );

  @GetMapping("/applications")
  ResponseEntity<RestResult<RestPageResponse<ApplicationResponse>>> getApplications(
    @RequestParam(required = false, defaultValue = "10") Integer size,
    @RequestParam(required = false, defaultValue = "0") Integer page,
    @RequestParam(required = false) Boolean enabled,
    @RequestParam(required = false) String businessType,
    @RequestParam(required = false) Long businessId,
    @RequestParam(required = false) String[] clientIds
  );

}
