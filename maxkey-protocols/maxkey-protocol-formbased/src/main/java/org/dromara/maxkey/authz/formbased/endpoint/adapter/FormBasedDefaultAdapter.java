/*
 * Copyright [2020] [MaxKey of copyright http://www.maxkey.top]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.dromara.maxkey.authz.formbased.endpoint.adapter;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.dromara.maxkey.authz.endpoint.adapter.AbstractAuthorizeAdapter;
import org.dromara.maxkey.constants.ConstsBoolean;
import org.dromara.maxkey.crypto.DigestUtils;
import org.dromara.maxkey.entity.apps.AppsFormBasedDetails;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

public class FormBasedDefaultAdapter extends AbstractAuthorizeAdapter {

  static String _HEX = "_HEX";

  @Override
  public Object generateInfo() {
    return null;
  }

  @Override
  public ModelAndView authorize(ModelAndView modelAndView, HttpServletResponse httpServletResponse) {
    modelAndView.setViewName("authorize/formbased_sso_submint");
    AppsFormBasedDetails details = (AppsFormBasedDetails) app;

    String password = account.getRelatedPassword();
    String passwordAlgorithm = details.getPasswordAlgorithm();

    if (StringUtils.isBlank(passwordAlgorithm)
      || passwordAlgorithm.equalsIgnoreCase("NONE")) {
      // TODO do nothing
    } else if (passwordAlgorithm.indexOf(_HEX) > -1) {
      passwordAlgorithm = passwordAlgorithm.substring(0, passwordAlgorithm.indexOf(_HEX));
      password = DigestUtils.digestHex(account.getRelatedPassword(), passwordAlgorithm);
    } else {
      password = DigestUtils.digestBase64(account.getRelatedPassword(), passwordAlgorithm);
    }

    modelAndView.addObject("id", details.getId());
    modelAndView.addObject("action", details.getRedirectUri());
    modelAndView.addObject("redirectUri", details.getRedirectUri());
//    modelAndView.addObject("action", "http://172.31.0.7:9123/sqlworkflow/");
//    modelAndView.addObject("redirectUri", "http://172.31.0.7:9123/sqlworkflow/");
    modelAndView.addObject("loginUrl", details.getLoginUrl());
    modelAndView.addObject("usernameMapping", details.getUsernameMapping());
    modelAndView.addObject("passwordMapping", details.getPasswordMapping());
    modelAndView.addObject("username", account.getRelatedUsername());
    modelAndView.addObject("password", password);
    modelAndView.addObject("timestamp", "" + Instant.now().getEpochSecond());

    if (ConstsBoolean.isTrue(details.getIsExtendAttr())) {
      modelAndView.addObject("extendAttr", details.getExtendAttr());
      modelAndView.addObject("isExtendAttr", true);
    } else {
      modelAndView.addObject("isExtendAttr", false);
    }

    if (StringUtils.isNotBlank(details.getAuthorizeView())) {
        modelAndView.setViewName("authorize/" + details.getAuthorizeView());
      }
    

    return modelAndView;
  }

}
