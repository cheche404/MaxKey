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

import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;
import org.dromara.maxkey.authz.endpoint.adapter.AbstractAuthorizeAdapter;
import org.dromara.maxkey.crypto.DigestUtils;
import org.dromara.maxkey.entity.apps.AppsFormBasedDetails;
import org.springframework.web.servlet.ModelAndView;

public class FormBasedNeteaseNoteYoudaoAdapter extends AbstractAuthorizeAdapter {

	@Override
	public Object generateInfo() {
		return null;
	}


	@Override
	public ModelAndView authorize(ModelAndView modelAndView, HttpServletResponse httpServletResponse) {
		modelAndView.setViewName("authorize/formbased_wy_youdao_sso_submint");
		AppsFormBasedDetails details=(AppsFormBasedDetails)app;
		modelAndView.addObject("username", account.getRelatedUsername());
		modelAndView.addObject("password",  DigestUtils.md5Hex(account.getRelatedPassword()));
		modelAndView.addObject("currentTime",  (new Date()).getTime());
		return modelAndView;
	}

}
