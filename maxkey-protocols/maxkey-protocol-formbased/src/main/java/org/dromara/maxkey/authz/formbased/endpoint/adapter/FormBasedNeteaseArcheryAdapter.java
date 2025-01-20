package org.dromara.maxkey.authz.formbased.endpoint.adapter;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.dromara.maxkey.authz.endpoint.adapter.AbstractAuthorizeAdapter;
import org.dromara.maxkey.constants.ConstsBoolean;
import org.dromara.maxkey.crypto.DigestUtils;
import org.dromara.maxkey.entity.apps.AppsFormBasedDetails;
import org.springframework.web.servlet.ModelAndView;

import java.time.Instant;

/**
 * @author fudy
 * @date 2025/1/14
 */
public class FormBasedNeteaseArcheryAdapter extends AbstractAuthorizeAdapter {

  static String _HEX = "_HEX";

  @Override
  public Object generateInfo() {
    return null;
  }

  @Override
  public ModelAndView authorize(ModelAndView modelAndView, HttpServletResponse httpServletResponse) {
    modelAndView.setViewName("authorize/formbased_sso_archery_submint");
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
